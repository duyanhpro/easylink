package easylink.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import easylink.common.Constant;
import easylink.dto.ActionCreateAlarm;
import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.Rule;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import org.springframework.web.context.request.RequestContextHolder;
import vn.vivas.core.util.JsonUtil;

public class RuleExecutionRunnable implements Runnable {

	Logger log = LoggerFactory.getLogger(this.getClass());

	Rule r;
	Queue<Map<String,Object>> msgQueue;
	Map<String, Long> actionTime = new HashMap<String, Long>();
	
	Expression exp;
	static final long WAIT_TIME = 1000;
	
	public RuleExecutionRunnable(Rule r, Queue<Map<String, Object>> msgQueue) {
		super();
		this.r = r;
		this.msgQueue = msgQueue;
	}
	
	// 1. update rule when changes; 2. clear thread when rule is deleted or not used  --> DONE:  do it by calling static methods in MqttService
	// Loop:  take message from queue, then process
	@Override
	public void run() {
		log.info("Start new execution thread for rule {} {}", r.getId(), r.getName());

		try {
			while (true) {
				Map<String, Object> map = msgQueue.poll();
				if (map == null) {
					try {
						Thread.currentThread().sleep(WAIT_TIME);
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
					continue;
				}
				log.trace("Checking rule {}, condition {} on event {}", r.getName(), r.getCondition(), map);

				if (exp == null) {
					ExpressionParser parser = new SpelExpressionParser();
					exp = parser.parseExpression(r.getCondition());
				}
				StandardEvaluationContext context = new StandardEvaluationContext();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					context.setVariable(entry.getKey(),entry.getValue());
				}
				boolean b = false;
				try {
					 b = exp.getValue(context, Boolean.class);
				} catch (Exception e) {
					log.debug("Condition Expression Invalid " + e.getMessage());
				}
				try {
					if (b) {
						executeAction(r, map);
					}
				}	catch (Exception e) {
					log.debug("Exeception when execute rule action " + e.getMessage());
				}
			}
		} catch (ParseException e) {
			log.info("Stop thread with exception {}", e.getMessage());
		}
	}
	
	public void executeAction(Rule rule, Map<String, Object> message) {

		//Check limit time between 2 consecutive actions 
		String deviceToken = (String) message.get(Constant.DEVICE_TOKEN);
		Long lastTime = actionTime.get(rule.getId()+"-"+deviceToken);
		if (lastTime != null && ((System.currentTimeMillis()-lastTime)<(rule.getMinInterval()*1000))) {
			return;
		}
		actionTime.put(rule.getId() + "-" + deviceToken, System.currentTimeMillis());
		
		//log.debug("Execute action {}", rule.getAction());
		ActionCreateAlarm ac = JsonUtil.parse(rule.getAction(), ActionCreateAlarm.class);
		if (rule.getActionType().equals("CreateAlarm")) {
			Alarm a = new Alarm((String)message.get(Constant.DEVICE_TOKEN),new Date(), ac.getAlarmContent(),ac.getAlarmType(),
					AlarmLevel.valueOf(ac.getAlarmLevel()), rule.getId(), (Date) message.get("event_time"));
			log.info("Create alarm {}", a);	
			
			AlarmService as = BeanUtil.getBean(AlarmService.class);
			as.createAlarm(a);
			
		} else {
			log.error("Unknown action type!");
		}
	}

}
