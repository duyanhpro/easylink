package easylink.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class RuleExecutionServie {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RuleService ruleService;
	@Autowired
	DeviceTypeService deviceTypeService;
	@Autowired
	AlarmService alarmService;
	@Autowired
	RedisService redisService;

	// map ruleId with message queue.  Each rule will have one queue to process messages
	static Map<Integer, Queue<Map<String,Object>>> msgQueueMap = new HashMap<>();	

	// Map thread with rule Id. Each rule will have 1 thread to process messages (from its own queue)
	static Map<Integer, Thread> ruleExecutionThreadMap = new HashMap<>();			 

	static boolean someRuleIsUpdating = false;
	/**
	 * Execute rule check when receive MQTT event
	 * @param deviceToken
	 * @param telemetryData
	 */
	public void executeRule(String deviceToken, Map<String, Object> telemetryData) {
		// Flow:  iterate all rules, find rule that apply for this deviceToken, add msg to queue -->
		// RuleExecutionRunnable:  thread xử lý check rule
		List<Rule> lr = ruleService.findAllActive();
		for (Rule r: lr) {
			// Check if rule apply to this device
			if (!ruleService.isApplied(r, deviceToken))
				continue;

			// Check if rule is updating then just wait 1 sec (ballpark number, just for prevention)
			if (someRuleIsUpdating) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}

			log.trace("Checking rule {} {} for event {}", r.getId(), r.getName(), telemetryData);
			Queue<Map<String, Object>> q = msgQueueMap.get(r.getId());
			if (q == null) {
				log.info("Create new message queue for rule {}", r);
				// create the queue
				q = new ConcurrentLinkedQueue<>();
				msgQueueMap.put(r.getId(), q);
				q.add(telemetryData);
				// initialize thread to process this queue
				Thread t = new Thread(new RuleExecutionRunnable(r, q));
				t.start();
				ruleExecutionThreadMap.put(r.getId(), t);
			} else
				q.add(telemetryData);
		}
	}
	
	// clean up thread when rule is not used
	// todo:  make it distributed (update all nodes)
	public void disableRule(int ruleId) {	
		log.info("Stop rule execution thread for rule {}", ruleId);
		Thread t = ruleExecutionThreadMap.get(ruleId);
		if (t!=null) t.interrupt();
		
		ruleExecutionThreadMap.remove(ruleId);
		msgQueueMap.remove(ruleId);
	}

	// update rule condition, etc... So we kill the thread and start with new parameters
	public void updateRule(int ruleId) {
		someRuleIsUpdating = true;

		disableRule(ruleId);
		Queue<Map<String, Object>> q = new ConcurrentLinkedQueue<>();
		msgQueueMap.put(ruleId, q);
		// initialize thread to process this queue
		Thread t = new Thread(new RuleExecutionRunnable(ruleService.findById(ruleId), q));
		t.start();
		ruleExecutionThreadMap.put(ruleId, t);

		someRuleIsUpdating = false;
	}


//	public void checkSystemRule2(String deviceToken, Map<String, Object> telemetryData) {
//		// Check connection re-connect rule
//
//		// Check device error rule
//		// find deviceType.sensors.  Then iterate and check if any of its are "error"
//		// send alarm if this is first time
//		String sensors = deviceTypeService.findUsedSensorsFromDeviceToken(deviceToken);
//		String[] ss = sensors.split(",");
//		for (String s: ss) {
//			Object o = telemetryData.get(s);
//			if (o == null || o.equals("error")) {		// sensor error
//				// Check if not yet alarm
//				Boolean sensorError = (Boolean) redisService.get("sensor-error:" + s+":" + deviceToken);
//				if (sensorError == null) {
//					log.warn("Field {} error", s);
//					redisService.put("sensor-error:" + s+":" + deviceToken, true);
//					// Create alarm
//					Alarm a = new Alarm(deviceToken, new Date(), "Cảm biến " + s + " bị lỗi", "Sensor",
//							AlarmLevel.Error, 0, null);
//					alarmService.createAlarm(a);
//				}
//			} else {			// sensor normal
//				// Check if sensor is recovered
//				Boolean sensorError = (Boolean) redisService.get("sensor-error:" + s+":" + deviceToken);
//				if (sensorError != null && sensorError) {
//					// change status and send alarm
//					redisService.delete("sensor-error:" + s+":" + deviceToken);
//					// Create alarm
//					Alarm a = new Alarm(deviceToken, new Date(), "Cảm biến " + s + " hoạt động lại", "Sensor",
//							AlarmLevel.Info, 0, null);
//					alarmService.createAlarm(a);
//				}
//			}
//		}
//	}

	/**
	 * Connection rule & sensor error rule
	 * @param deviceToken
	 * @param telemetryData
	 */
	public void checkSystemRule(String deviceToken, Map<String, Object> telemetryData) {
		// Check connection re-connect rule

		Set<String> errorSensors = (Set<String>) redisService.get("sensor-error:" + deviceToken);
		if (errorSensors == null) errorSensors = new HashSet<String>();

		// Check device error rule
		// find deviceType.sensors.  Then iterate and check if any of its are "error"
		// send alarm if this is first time
		String usedSensors = deviceTypeService.findUsedSensorsFromDeviceToken(deviceToken);
		String[] ss = usedSensors.split(",");
		for (String s: ss) {
			Object o = telemetryData.get(s);
			if (o == null || o.equals("error")) {		// sensor error
				// Check if not yet alarm
				if (!errorSensors.contains(s)) {
						log.warn("Field {} error", s);
						errorSensors.add(s);
						// Create alarm
						Alarm a = new Alarm(deviceToken, new Date(), "Cảm biến " + s + " bị lỗi", "Sensor",
								AlarmLevel.Error, 0, null);
						alarmService.createAlarm(a);

				}
			} else {			// sensor normal
				// Check if sensor is recovered
				if (errorSensors.contains(s)) {
					errorSensors.remove(s);
					// Create alarm
					Alarm a = new Alarm(deviceToken, new Date(), "Cảm biến " + s + " hoạt động lại", "Sensor",
							AlarmLevel.Info, 0, null);
					alarmService.createAlarm(a);
				}
			}
		}
		redisService.put("sensor-error:" + deviceToken, errorSensors);
	}
}
