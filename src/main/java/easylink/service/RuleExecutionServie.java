package easylink.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	
	// map ruleId with message queue.  Each rule will have one queue to process messages
	static Map<Integer, Queue<Map<String,Object>>> msgQueueMap = new HashMap<>();	

	// Map thread with rule Id. Each rule will have 1 thread to process messages (from its own queue)
	static Map<Integer, Thread> ruleExecutionThreadMap = new HashMap<>();			 

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
			log.trace("Checking rule {} for event {}", r.getName(), telemetryData);
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
	public void disableRule(int ruleId) {	
		log.info("Stop rule execution thread for rule {}", ruleId);
		Thread t = ruleExecutionThreadMap.get(ruleId);
		if (t!=null) t.interrupt();
		
		ruleExecutionThreadMap.remove(ruleId);
		msgQueueMap.remove(ruleId);
	}

	// update rule condition, etc... So we kill the thread and start with new parameters
	public void updateRule(Rule r) {		
		disableRule(r.getId());
		Queue<Map<String, Object>> q = new ConcurrentLinkedQueue<>();
		msgQueueMap.put(r.getId(), q);
		// initialize thread to process this queue
		Thread t = new Thread(new RuleExecutionRunnable(r, q));
		t.start();
		ruleExecutionThreadMap.put(r.getId(), t);
	}
}
