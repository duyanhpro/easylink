package easylink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.dto.ActionCreateAlarm;
import easylink.dto.AlarmLevel;
import easylink.dto.ConnectionStatus;
import easylink.entity.Alarm;
import easylink.entity.DeviceStatus;
import easylink.repository.DeviceRepository;
import easylink.repository.DeviceStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableAsync
@Transactional
public class DeviceStatusService {
    @Autowired
    DeviceStatusRepository statusRepo;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceRepository repo;
    @Autowired
    AlarmService alarmService;
    @Autowired
    MqttService mqttService;
    @Autowired
    RedisService redisService;
    @Autowired
    ConfigParamService configParamService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    Map<String, Long> lastStatusUpdateTime = new ConcurrentHashMap<>();     //Device token to Last event time (epoch)
    List<String> deviceTokens;  // all active device tokens
    Map<String, Integer> statusMap = new ConcurrentHashMap<>();     // device token to last status

//    @Value("${mqtt.device.timeout}")
//    int timeOut = 10;  // in seconds

    @Value("${mqtt.topic.connection}")
    String connectionTopic = "connection";


    public List<DeviceStatus> findAllStatus() {
        return statusRepo.findAll();
    }
    public List<DeviceStatus> findStatusOfActiveDevices() {
        return statusRepo.findActiveDevices();
    }

    /**
     * Find status. Convert json to hashmap to display on screen
     * @param deviceToken
     * @return
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public DeviceStatus findStatus(String deviceToken)  {
        DeviceStatus ss = statusRepo.findStatus(deviceToken);
        //System.out.println(ss);
        if (ss == null) {
            return new DeviceStatus(deviceToken);
        }
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(ss.getTelemetry(), Map.class);
        } catch (Exception e) {

            //  ignore, just return empty map
        }

        log.debug("Loaded status:  {}, status = {}", map, ss.getStatus());
        ss.setTelemetryMap(map);

        return ss;
    }

    /**
     * Update device status when receive event from MQTT
     * @param deviceToken
     * @param map   raw data fields
     * @param msg
     */
    public void updateDeviceStatus(String deviceToken, Map<String, Object> map, String msg) {
        // TODO: handle it in batch, in a queue
        try {
            log.trace("Saving device status: {}", map);

            // Update time for monitor worker
            lastStatusUpdateTime.put(deviceToken, System.currentTimeMillis());
            Date eventTime = new Date();
            DeviceStatus st = new DeviceStatus();
            st.setDeviceToken(deviceToken);
            st.setEventTime(eventTime);
            st.setTelemetry(msg);
            st.setStatus(DeviceStatus.STATUS_OK);   // status:  connection status from gateway to monitoring device

            // Save to DB
            statusRepo.save(st);

            // Check last status and notify if this is a connection recovery
            ConnectionStatus lastStatus = (ConnectionStatus) redisService.get("connection-" + deviceToken);
            if (lastStatus != null) {
                if (lastStatus.getStatus() == ConnectionStatus.NOK) {   // change from NOK to OK
                    // send alarm info about connection recovery
                    // notify that connection is now OK
                    mqttService.sendToMqtt(connectionTopic + "/" + deviceToken, "OK");

                    // Create alarm
                    ActionCreateAlarm ac = configParamService.getAlarmConfig("ALARM_CONNECTION_RECOVER");
                    Alarm a;
                    if (ac == null)
                        a = new Alarm(deviceToken,new Date(), "Trạm khôi phục kết nối","Kết nối",
                                AlarmLevel.Info, 0, eventTime);
                    else
                        a = new Alarm(deviceToken, new Date(), ac.getAlarmContent(), ac.getAlarmType(),
                                AlarmLevel.valueOf(ac.getAlarmLevel()), 0, null);

                    alarmService.createAlarm(a);
                }
            }

            // Save connection status to redis
            ConnectionStatus status = new ConnectionStatus();
            status.setStatus(ConnectionStatus.OK);
            status.setLastEventTime(System.currentTimeMillis());
            status.setSentLostConnectionAlarm(false);
            redisService.put("connection-" + deviceToken, status);

        } catch (Exception e) {
            log.error("Exception when updateDeviceStatus: {}", e.getMessage());
        }
    }

    long first = 0;

    @Scheduled(fixedDelayString = "${monitor.interval}", initialDelay = 30000)
    @Transactional
    public void monitorDeviceConnectionRedis() {

        // 1. Get all devices with status = OK and being active from DB (or redis doesn't matter)
        // only need to check device with status = OK. Ignore device that never received any data.  NOK device will change to OK when new event comes
        List<String> activeTokens = statusRepo.findActiveDeviceTokenByDeviceStatus(DeviceStatus.STATUS_OK);
        log.trace("Check device connections. Device tokens with status = OK: {}", activeTokens);
        Long timeOut = (Long)configParamService.getConfig("CONNECTION_TIMEOUT");

        // 2. Iterate each device token, create lock per device, check timeout, if yes set last checked time.  If can not get lock (ie other node processing it), just
        // continue to next device token
        for (String token: activeTokens) {
            String lockKey = "timeout-check-" + token;
            Boolean getLock = redisService.acquireLock(lockKey);
            //log.trace("get lock: " + getLock);
            if (!getLock) continue;     // just continue to next device, let whichever process locking the key do their job
            ConnectionStatus status = (ConnectionStatus) redisService.get("connection-" + token);
            log.trace("Device {} connection status {}", token, status);
            if (status == null) {
                continue;   // this device has never received any data. Ignore it.
            }
            Long now = System.currentTimeMillis();

            if (status.getStatus() == ConnectionStatus.OK) {
                if (!status.isSentLostConnectionAlarm() && (now - status.getLastEventTime() > timeOut * 1000)) {      // neu timeout va chua gui alarm lan nao
                    // timeout --> update DB, send mqtt & alarm
                    log.info("No message from Device {} after timeout {}s -> Update status to NOK", token, timeOut);
                    statusRepo.updateStatus(token, DeviceStatus.STATUS_NOK);

                    // Create alarm
                    ActionCreateAlarm ac = configParamService.getAlarmConfig("ALARM_CONNECTION_ERROR");
                    Alarm a;
                    if (ac == null)
                        a = new Alarm(token, new Date(), "Trạm mất kết nối", "Kết nối",
                                AlarmLevel.Error, 0, null);
                    else
                        a = new Alarm(token, new Date(), ac.getAlarmContent(), ac.getAlarmType(),
                                AlarmLevel.valueOf(ac.getAlarmLevel()), 0, null);

                    alarmService.createAlarm(a);

                    // Optional: send an event to mqtt connection monitor topic
                    mqttService.sendToMqtt(connectionTopic + "/" + token, "NOK");

                    status.setStatus(ConnectionStatus.NOK);
                    status.setSentLostConnectionAlarm(true);
                    status.setLastAlarmTime(now);
                    redisService.put("connection-" + token, status);
                }

                redisService.releaseLock(lockKey);
            }

        }
    }

}
