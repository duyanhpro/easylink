package easylink.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import easylink.dto.RuleMetadata;
import easylink.entity.Device;
import easylink.entity.Rule;
import easylink.entity.RuleDevice;
import easylink.entity.RuleGroup;
import easylink.repository.RuleDeviceRepository;
import easylink.repository.RuleGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.repository.RuleRepository;
import easylink.repository.DeviceRepository;
import vn.vivas.core.util.JsonUtil;

@Service
@Transactional
public class RuleService {
	
	static Logger log = LoggerFactory.getLogger(RuleService.class);
	@Autowired
	RuleRepository repo;
	
	@Autowired
	RuleExecutionServie executionService;
	
	@Autowired
    DeviceRepository deviceRepo;
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	RuleDeviceRepository rsRepo;
	@Autowired
	RuleGroupRepository rgRepo;
	@Autowired
	MqttService mqttService;

	@Value("${mqtt.topic.system}")
	String systemTopic;

	public List<Rule> fakeRule() {
		List<Rule> lr = new ArrayList<Rule>();
		lr.add(new Rule(1, "rule 1","temp > 1000", "nhiệt độ hơi cao;Warning","CreateAlarm",Rule.STATUS_ACTIVE, 0));
		lr.add(new Rule(2, "rule 2","temp > 3000 or humid > 80", "Trạng thái nguy hiểm, nên dừng hoạt động ngay;Critical","CreateAlarm",Rule.STATUS_ACTIVE,0));		
		return lr;
	}

	public List<Rule> findAll() {
		return repo.findAll(Sort.by(Direction.DESC, "createdDate"));
	}

	@Cacheable("activeRules")
	public List<Rule> findAllActive() {
		return repo.findByStatus(Rule.STATUS_ACTIVE);
	}
	
	public List<Integer> findAppliedDeviceIds(int ruleId) {
		return repo.findAppliedDeviceIds(ruleId);
	}
	public List<Integer> findAppliedGroupIds(int ruleId) { return repo.findAppliedGroupIds(ruleId); }

	//@Caching(evict = {@CacheEvict("activeRules"), @CacheEvict(value="directory", key="#student.id") })
	@CacheEvict(value = {"activeRules", "ruleDeviceToken"}, allEntries = true)
	public Rule save(Rule r) {
		log.debug("Saving rule {}", r);
		r = repo.save(r);
		
		// update rule execution thread
		if (r.getStatus() == Rule.STATUS_INACTIVE)
			//executionService.disableRule(r.getId());
			sendMqttRuleChange("disable", r.getId());
		else 
			//executionService.updateRule(r.getId());
			sendMqttRuleChange("update", r.getId());
		
		return r;
	}
	public void sendMqttRuleChange(String action, int ruleId) {
		mqttService.sendToMqtt(systemTopic + "/rule", action + ":" + ruleId, 2);
	}

	public Rule findById(int ruleId) {
		return repo.findById(ruleId).get();
	}
	
	public void delete(int ruleId) {

		// delete rule_device link
		repo.deleteRuleDeviceLink(ruleId);

		// delete rule_group link
		repo.deleteRuleGroupLink(ruleId);

		repo.deleteById(ruleId);
		
		// Cleanup rule execution thread
		//executionService.disableRule(ruleId);
		sendMqttRuleChange("disable", ruleId);
	}

	// Check if rule apply to this device
	@Cacheable(value = "ruleDeviceToken", key =  "#r.id + '-' + #deviceToken")
	public boolean isApplied(Rule r, String deviceToken) {

		// Find devices from rule_device, then check if device is applied
		List<String> deviceTokens = repo.findDeviceTokenByRule(r.getId());
		if (deviceTokens.contains(deviceToken))
			return true;

		// Find devices from rule_group, then check
		if (r.getScope() == Rule.SCOPE_RECURSIVE_GROUP)
			deviceTokens = repo.findDeviceTokenByRuleFromGroup(r.getId());
		else
			deviceTokens = repo.findDeviceTokenByRuleFromGroupNoRecursive(r.getId());
		if (deviceTokens.contains(deviceToken)) {
			// Find deviceTypes and check if applied (all in these groups of this type is applied)
			if (r.getMetadata() != null) {
				RuleMetadata m = JsonUtil.parse(r.getMetadata(), RuleMetadata.class);
				Device d = deviceService.findByToken(deviceToken);
				if (m.getDeviceTypes().contains(d.getTypeId()))
					return true;
			} else { 	// no limit deviceTypes
				return true;
			}
		}

		return false;
	}

	public void saveRuleDeviceLink(Rule r, Integer[] deviceIds) {
		// remove all old rule-device link
		repo.deleteRuleDeviceLink(r.getId());
		
		if (deviceIds == null) return;
		// save new rule-device link
		for (int deviceId: deviceIds) {
			RuleDevice rs = new RuleDevice(r.getId(), deviceId);
			log.debug("Add relation {}", rs);
			rsRepo.save(rs);
		}
	}
	public void saveRuleGroupLink(Rule r, Integer[] groupIds) {
		// remove all old rule-device link
		repo.deleteRuleGroupLink(r.getId());

		if (groupIds == null) return;
		// save new rule-device link
		for (int groupId: groupIds) {
			RuleGroup rg = new RuleGroup(r.getId(), groupId);
			log.debug("Add relation {}", rg);
			rgRepo.save(rg);
		}
	}

//	public int findCommonParent(List<Integer> groupIds) {
//
//	}

//	Map<Integer, Set<String>> ruleDeviceTokenMap = new ConcurrentHashMap<>(); //  map ruleId to Set<device_token>
//	// Utitilty to improve performance when check which rules apply to which devices  (can do in-mem and at start-up to increase performance even more!!!)
//	// TOdo: Need to update when:  CRUD device; CRUD group; CRUD rule!!!  Need to lock Set<> when update/refresh it
//	// --> too much. Probably just do caching!
//	public void generateAllRuleDeviceLink(Integer ruleId, Integer[] deviceIds, Integer[] groupIds) {
//		Set<String> s = ruleDeviceTokenMap.get(ruleId);
//		if (s==null) s = new HashSet<>();
//		for (int id: deviceIds) {
//			Device d = deviceService.findById(id);
//			.......
//		}
//	}

	/**
	 * Validate condition expression.  Return true if expression is valid
	 * @param condition
	 * @return
	 */
    public boolean validate(String condition) {
		log.info("Validating rule condition {}", condition);
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(condition);
		StandardEvaluationContext context = new StandardEvaluationContext();

		// create fake data object. Actually this will depend on schema!
		Map<String, Object> map = Map.of("event_time",new Date(), "vol1",20,"vol2",30,
				"cur1",3.5, "cur2",2.4, "door1",0, "door2",1,
				"hum1",40.5, "hum2",32.1); // max 10 items
//		map.put("temp1",42.5);
//		map.put("temp2", 39.6);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			context.setVariable(entry.getKey(),entry.getValue());
		}
		boolean b = false;
		try {
			b = exp.getValue(context, Boolean.class);		// Note:  #nonExistentVar will become null (Spring's design)
			log.debug("Condition result on fake data: " + b);
			return true;  // no exception means condition is valid
		} catch (Exception e) {
			log.debug("Condition Expression Invalid " + e.getMessage());
			return false;
		}
    }

	/**
	 * Get permission to rule ID.  True if user can edit that rule, false or null if not
	 * User in same group or parent group can edit.
	 * @return
	 */
	public Map<Integer, Boolean> getMyPermission() {
		// Get list of group that this user have access

		// Get list of rule and the group of its created user!

		// build result map

		return null;
	}
}
