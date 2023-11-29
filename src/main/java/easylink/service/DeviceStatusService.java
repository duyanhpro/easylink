package easylink.service;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.dto.AlarmLevel;
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

    Logger log = LoggerFactory.getLogger(this.getClass());

    Map<String, Long> lastStatusUpdateTime = new ConcurrentHashMap<>();     //Device token to Last event time (epoch)
    List<String> deviceTokens;  // all active device tokens
    Map<String, Integer> statusMap = new ConcurrentHashMap<>();     // device token to last status

    //Map<>
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
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
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
        // TODO 2: use Redis to store it
        try {
            log.trace("Saving device status: {}", map);

            // Update time for monitor worker
            lastStatusUpdateTime.put(deviceToken, System.currentTimeMillis());

            DeviceStatus st = new DeviceStatus();
            st.setDeviceToken(deviceToken);
            st.setEventTime(new Date());
            st.setTelemetry(msg);
            st.setStatus(DeviceStatus.STATUS_OK);   // status:  connection status from gateway to monitoring device
            statusRepo.save(st);

            // todo: check last status, if NOK to OK then create an Info alarm. To simplify: use a map, skip case when restart server
            Integer lastStatus = statusMap.get(deviceToken);
            if ((lastStatus != null) && (lastStatus == DeviceStatus.STATUS_NOK)) {
                // notify that connection is now OK
                mqttService.sendToMqtt(connectionTopic + "/" + deviceToken, "0");

                // Create alarm
                Alarm a = new Alarm(deviceToken,new Date(), "Trạm khôi phục kết nối","Connection",
                        AlarmLevel.Info, 0);
                alarmService.createAlarm(a);
            }
            statusMap.put(deviceToken, DeviceStatus.STATUS_OK);

        } catch (Exception e) {
            log.error("Exception when updateDeviceStatus: {}", e.getMessage());
        }
    }

    public DeviceStatus updateStatus(DeviceStatus st) {
        return statusRepo.save(st);
    }

    public List<DeviceStatus> findAllStatus() {
        return statusRepo.findAll();
    }
    public List<DeviceStatus> findStatusOfActiveDevices() {
        return statusRepo.findActiveDevices();
    }

    @Value("${mqtt.device.timeout}")
    int timeOut = 10;  // in seconds
    long first = 0;

    @Value("${mqtt.topic.connection}")
    String connectionTopic = "connection";

    @Scheduled(fixedDelayString = "${monitor.interval}")
    @Transactional
    public void monitorDeviceConnection() {
        log.trace("Check device connections");
        // if just start app?  Wait X sec after 1st check ( = TIME_OUT)
        if (first == 0) {
            first = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() - first < timeOut * 1000)
            return;

        // only need to check device with status = OK. Because NOK device will change to OK when new event comes
        // TODO: event info when connection resumed?
        // TODO: cache this (not so important)
        List<String> activeTokens = statusRepo.findTokenByStatus(DeviceStatus.STATUS_OK);

        // iterate all tokens, check if (now - lastUpdate) >= TIME_OUT then update status = NOK in DB
        for (String token: activeTokens) {
            Long lastTime = lastStatusUpdateTime.get(token);
            Long now = System.currentTimeMillis();
            if ((lastTime != null) && (now - lastTime < timeOut * 1000))
                continue;
            // else device timeout
            log.info("No message from Device {} after timeout {}s -> Update status to NOK", token, timeOut);
            statusRepo.updateStatus(token, DeviceStatus.STATUS_NOK);
            statusMap.put(token, DeviceStatus.STATUS_OK);

            // Create alarm
            Alarm a = new Alarm(token,new Date(), "Trạm mất kết nối","Connection",
                    AlarmLevel.Error, 0);
            alarmService.createAlarm(a);

            // Optional: send an event to mqtt connection monitor topic
            mqttService.sendToMqtt(connectionTopic + "/" + token, "0");

        }
    }
}
