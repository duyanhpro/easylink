package easylink.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import easylink.entity.Device;
import easylink.entity.Rule;
import easylink.entity.RuleDevice;
import easylink.entity.RuleGroup;
import easylink.repository.RuleDeviceRepository;
import easylink.repository.RuleGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<Rule> fakeRule() {
		List<Rule> lr = new ArrayList<Rule>();
		lr.add(new Rule(1, "rule 1","temp > 1000", "nhiệt độ hơi cao;Warning","CreateAlarm",Rule.STATUS_ACTIVE, 0));
		lr.add(new Rule(2, "rule 2","temp > 3000 or humid > 80", "Trạng thái nguy hiểm, nên dừng hoạt động ngay;Critical","CreateAlarm",Rule.STATUS_ACTIVE,0));		
		return lr;
	}

	public List<Rule> findAll() {
		return repo.findAll(Sort.by(Direction.ASC, "modifiedDate"));
	}

	// TODO: cache this
	public List<Rule> findAllActive() {
		return repo.findByStatus(Rule.STATUS_ACTIVE);
	}
	
	public List<Integer> findAppliedDeviceIds(int ruleId) {
		return repo.findAppliedDeviceIds(ruleId);
	}
	public List<Integer> findAppliedGroupIds(int ruleId) { return repo.findAppliedGroupIds(ruleId); }
	
	public Rule save(Rule r) {
		log.debug("Saving new rule {}", r);
		r = repo.save(r);
		
		// update rule execution thread
		if (r.getStatus() == Rule.STATUS_INACTIVE)
			executionService.disableRule(r.getId());
		else 
			executionService.updateRule(r);
		
		return r;
	}
	
	public Rule findById(int ruleId) {
		return repo.findById(ruleId).get();
	}

	public List<Rule> findRuleByDeviceId(String deviceToken) {
		// TODO:  also return rules that apply to all devices
		return repo.findByDeviceToken(deviceToken);
	}
	
	public void delete(int ruleId) {
		repo.deleteById(ruleId);
		
		// Cleanup rule execution thread
		executionService.disableRule(ruleId);
	}

	// Check if rule apply to this device
	// TODO: cache this
	public boolean isApplied(Rule r, String deviceToken) {
		if (r.getScope() == Rule.SCOPE_ALL_DEVICES)
			return true;
		// Find from rule_device and rule_group, then merge 2 list to find devices
		List<String> deviceTokens = repo.findDeviceTokenByRule(r.getId());
		if (deviceTokens.contains(deviceToken))
			return true;

		// Find from rule_group, then check
		deviceTokens = repo.findDeviceTokenByRuleFromGroup(r.getId());
		if (deviceTokens.contains(deviceToken))
			return true;

		return false;
	}

	public void saveRuleDeviceLink(Rule r, Integer[] deviceIds) {
		// remove all old rule-device link
		repo.deleteRuleDeviceLink(r.getId());
		
		if (deviceIds == null) return;
		// save new rule-device link
		for (int deviceId: deviceIds) {
			String deviceToken = deviceRepo.findById(deviceId).get().getDeviceToken();
			RuleDevice rs = new RuleDevice(r.getId(), deviceId, deviceToken);
			log.debug("Add relation {}", rs);
			rsRepo.save(rs);
		}
	}
	public void saveRuleGroupLink(Rule r, Integer[] groupIds) {
		// remove all old rule-device link
		repo.deleteRuleGroupLink(r.getId());
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
