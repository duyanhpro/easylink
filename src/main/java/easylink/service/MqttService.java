package easylink.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import easylink.common.Constant;
import easylink.entity.Device;
import easylink.entity.DeviceSchema;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

	@Value("${mqtt.serverUrl}")
	String mqttServerUrl;

	@Value("${mqtt.username}")
	String mqttUsername;

	@Value("${mqtt.password}")
	String mqttPassword;

	@Value("${mqtt.enable}")
	Boolean mqttEnable;

	@Value("${raw.insert.enable:true}")
	Boolean enableInsert;
	@Value("${raw.save-status.enable:true}")
	Boolean enableSaveStatus;
	@Value("${rule.enable:true}")
	Boolean enableRule;

	@Autowired
    DeviceService deviceService;
	@Autowired
	DeviceStatusService deviceStatusService;
	@Autowired
	DeviceSchemaService schemaService;

	IMqttClient client;

	@Autowired
	RuleExecutionServie ruleExecutionService;

	@Autowired
	StarrocksService ingestService;

	static Logger log = LoggerFactory.getLogger(MqttService.class);

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!mqttEnable) return;
		log.info("Application context refreshed");
		connectMqtt();
		subscribeTelemetry();
	}

	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor()
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("MqttAsync-");
		executor.initialize();
		return executor;
	}

	public void connectMqtt() {
		String clientId = UUID.randomUUID().toString();
		try {
			client = new MqttClient(mqttServerUrl,clientId, new MemoryPersistence());
						// added new MemoryPersistence() to prevent FileLock warning
			MqttConnectOptions options = new MqttConnectOptions();
			options.setUserName(mqttUsername);
			options.setPassword(mqttPassword.toCharArray());
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			client.connect(options);

			log.info("MQTT connected");

		} catch (MqttException e) {
			log.error("MQTT connect exception ",e);
		}
	}

	@Scheduled(fixedRate = 30000)	// Keepalive. check MQTT connection every 30s
	public void checkMqttConnection() {
		if (!mqttEnable) return;
		log.trace("Mqtt watchdog");
		if (!client.isConnected())
			connectMqtt();
	}

	public void subscribeTelemetry() {
		List<DeviceSchema> ld = schemaService.findAll();
		for (DeviceSchema d: ld) {
			try {
				log.info("Subscribe to MQTT topic: " + d.getTopic());
				client.subscribe(d.getTopic(), 1);
				client.setCallback(new MqttCallback() {
					@Override
					public void connectionLost(final Throwable cause) {

					}

					@Override
					public void messageArrived(final String topic, final MqttMessage message) throws Exception {
						processTelemetryEvent(topic, new String(message.getPayload()));
					}

					@Override
					public void deliveryComplete(final IMqttDeliveryToken token) {

					}
				});
			} catch (MqttException e) {
				log.error("MQTT subscribe telemetry exception ", e);
			}
		}
	}

	public void processTelemetryEvent(String topic, String msg) {
		log.trace("Receive mqtt message: " + msg);
		// convert JSON string to Map
		ObjectMapper mapper = new ObjectMapper();

		int lastSlashIndex = topic.lastIndexOf('/');
		String deviceTokenFromTopic =  topic.substring(lastSlashIndex + 1);

		// check if device is disabled then discard event
		Device d = deviceService.findByToken(deviceTokenFromTopic);
		if (d.getStatus() == Device.STATUS_INACTIVE)
			return;

		try {
			Map<String, Object> map = mapper.readValue(msg, Map.class);

			boolean r = normalizedData(deviceTokenFromTopic, map);
			if (!r) return;		// skip this event

			if (enableSaveStatus)
				deviceStatusService.updateDeviceStatus(deviceTokenFromTopic, map, msg);
			if (enableInsert)
				ingestService.insertData(map);
			if (enableRule)
				ruleExecutionService.executeRule(deviceTokenFromTopic, map);
			
		} catch (Exception e) {
			log.error("Exception when processing event: {}", e);
		}
	}

	@Value("${event.max-delay-time}")
	int maxDelayTime = 60;	// Max time in seconds to wait for event. Reject older events
	public boolean normalizedData(String deviceToken, Map<String, Object> data) {
		// Read timestamp from message, reject if message too old (for example 1 hour late)
		Long t = (Long)data.get("event_time");
		Long now = System.currentTimeMillis();
		if ((t != null) && (now - t > maxDelayTime *1000))		// reject event
			return false;
		if (t == null)
			data.put("event_time", new Date());	// use system time if message does not have "event_time"
		if (!data.containsKey(Constant.DEVICE_TOKEN))
			data.put(Constant.DEVICE_TOKEN, deviceToken);  // get device token from mqtt topic

		return true;
	}

	public void sendToMqtt(String topic, String message) {
		log.debug("Publish mqtt message {} to topic {}", message, topic);
		try {
			if (!client.isConnected()) {
				connectMqtt();
				if (!client.isConnected())
					return;
			}
			MqttMessage msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
			msg.setQos(2);
			msg.setRetained(false);

			client.getTopic(topic).publish(msg);
		} catch (Exception e) {
			log.error("Exception when sending MQTT message to topic {}: {}", topic, e.getMessage());
		}
		return;
	}

}
