package easylink.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

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

	@Value("${mqtt.topic.telemetry}")
	String telemetryTopic;

	@Value("${mqtt.topic.rpc.request}")
	String rpcRequestTopic;

	@Value("${mqtt.topic.rpc.response}")
	String rpcResponseTopic;

	@Value("${mqtt.rpc.response.timeout:30}")
	int rpcReponseTimeout;

	@Value("${mqtt.enable}")
	Boolean mqttEnable;

	@Autowired
    DeviceService deviceService;

	IMqttClient client;

	@Autowired
	RuleExecutionServie ruleExecutionService;

	private BlockingQueue<String> rpcResponseQueue = new LinkedBlockingQueue<>();
	
	static Logger log = LoggerFactory.getLogger(MqttClient.class);

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!mqttEnable) return;
		log.info("Application context refreshed");
		connectMqtt();
		subscribeTelemetry();
		subscribeRpcResponse();
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

	@Scheduled(fixedRate = 3000)	// check every 30s
	public void checkMqttConnection() {
		if (!mqttEnable) return;
		log.debug("Mqtt watchdog");
		if (!client.isConnected())
			connectMqtt();
	}

	public void subscribeTelemetry() {
		try {
			client.subscribe(telemetryTopic, (topic, msg) -> {
				String body = new String(msg.getPayload());
				processTelemetryEvent(topic, body);
			});
		} catch (MqttException e) {
			log.error("MQTT subscribe telemetry exception ",e);
		}
	}

	public void subscribeRpcResponse() {
		try {
			client.subscribe(rpcResponseTopic, (topic, msg) -> {
				String body = new String(msg.getPayload());
				processRpcResponse(topic, body);
			});
		} catch (MqttException e) {
			log.error("MQTT subscribe telemetry exception ",e);
		}
	}

	public void processTelemetryEvent(String topic, String msg) {
		// convert JSON string to Map
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> map = mapper.readValue(msg, Map.class);
			String deviceToken =  (String) map.get("deviceToken");
			
			deviceService.updateDeviceStatus(deviceToken, map, msg);
			
			ruleExecutionService.executeRule(deviceToken, map);
			
		} catch (Exception e) {
			log.error("Exception when processing event: {}", e);
		}
	}

	public void processRpcResponse(String topic, String msg) {
		log.info("Receive rpc response in topic {}: {}", topic, msg);
		rpcResponseQueue.offer(msg);
	}

	public void sendToMqtt(String topic, String message) {

		if (!client.isConnected()) {
			connectMqtt();
			if (!client.isConnected())
				return;
		}
		MqttMessage msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
		msg.setQos(2);
		msg.setRetained(false);
		try {
			client.publish(topic, msg);
		} catch (MqttException e) {
			log.error("Exception when sending MQTT message to topic {}: {}", topic, e);
		}
		return;
	}

	public void sendRpcRequest(String deviceToken, String message) {
		sendToMqtt(rpcRequestTopic + deviceToken, message);
	}

	public String waitForRpcResponse(String deviceToken) {
		String response = null;
		try {
			response = rpcResponseQueue.poll(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
//	public void executeRule_basic(Map<String, Object> map) {
//	
//	// TODO: create 1 thread for each Rule to to avoid conflict
//	new Thread(new Runnable() {
//		
//		@Override
//		public void run() {
//			
//			// Find rules applied to this deviceToken
//			//List<Rule> lr = ruleService.findRuleByDeviceId((String)map.get("deviceToken"));
//			List<Rule> lr = ruleService.findAll();
//			for (Rule r: lr) {
//				log.debug("Checking condition {} on event {}", r.getCondition(), map);
//
//				Expression exp = expressionCache.get(r.getCondition());
//				if (exp == null) {
//					ExpressionParser parser = new SpelExpressionParser();
//					exp = parser.parseExpression(r.getCondition());
//					expressionCache.put(r.getCondition(), exp);
//				}
//				StandardEvaluationContext context = new StandardEvaluationContext();
//				for (Map.Entry<String, Object> entry : map.entrySet()) {
//				    context.setVariable(entry.getKey(),entry.getValue());
//				}
//				try {
//				boolean b = exp.getValue(context, Boolean.class);
//				//log.info("Condition check result: {}", b);
//				if (b) {
//					log.debug("Execute action {}", r.getAction());
//					
//				}
//				} catch (Exception e) {
//					log.debug("Condition Expression Invalid");
//				}
//			}
//		}
//	}).start();
//}
}
