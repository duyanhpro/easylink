package easylink.controller;

import easylink.dto.ActionCreateAlarm;
import easylink.dto.RuleAlarmDto;
import easylink.dto.RuleMetadata;
import easylink.entity.Rule;
import easylink.exception.AccessDeniedException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DeviceTypeService;
import easylink.service.RuleService;
import easylink.service.DeviceService;
import easylink.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vivas.core.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rule")
public class RuleController extends BaseController {

	@Autowired
    RuleService ruleService;
	
	@Autowired
	DeviceService deviceService;
	@Autowired
	DeviceTypeService deviceTypeService;

	@Autowired
	UserGroupService ugService;
	
	// List rule
	@GetMapping("")
	@NeedPermission("rule:list")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Quản lý luật cảnh báo");
		model.addAttribute("rules", ruleService.findAll());
		List<RuleAlarmDto> rl = new ArrayList<>();
		for (Rule r: ruleService.findAll()) {
			RuleAlarmDto ra = new RuleAlarmDto(r, JsonUtil.parse(r.getAction(), ActionCreateAlarm.class));
			rl.add(ra);
		}
		model.addAttribute("ras", rl);
		model.addAttribute("isRoot", ugService.isInRootGroup(SecurityUtil.getUserDetail().getUserId()));

		//model.addAttribute("permissions", ruleService.getMyPermission());
		return "rule/list";
	}
	
	@GetMapping("/view/{id}")
	@NeedPermission("rule:view")
	public String getRule(Model model, @PathVariable int id) {
		model.addAttribute("pageTitle", "Cấu hình luật");
		model.addAttribute("action", "update");
//		Rule r = new Rule(1, "rule 1","temp > 1000", "nhiệt độ hơi cao;Warning","CreateAlarm",Rule.STATUS_ACTIVE);
		Rule r = ruleService.findById(id);
		model.addAttribute("rule", r);
		model.addAttribute("alarm", (ActionCreateAlarm)JsonUtil.parse(r.getAction(), ActionCreateAlarm.class));
		model.addAttribute("devices", ruleService.findAppliedDeviceIds(id));	// applied devices
		model.addAttribute("allDevices", deviceService.findAll());
		model.addAttribute("groups", ruleService.findAppliedGroupIds(id));
		model.addAttribute("allGroups", groupService.findAll());
		model.addAttribute("metadata",
				r.getMetadata() == null? new RuleMetadata(): JsonUtil.parse(r.getMetadata(), RuleMetadata.class));
		model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
		return "rule/edit";
	}
	
	// create new rule
	@NeedPermission("rule:create")
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pageTitle", "Tạo mới luật");
		model.addAttribute("action", "create");
		model.addAttribute("rule", new Rule());
		model.addAttribute("alarm", new ActionCreateAlarm());
		model.addAttribute("allDevices", deviceService.findAll());
		model.addAttribute("allGroups", groupService.findAll());
		model.addAttribute("metadata", new RuleMetadata());
		model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
		return "rule/edit";
	}
	
	// Save rule (insert or update)
	@PostMapping("/save")
	@NeedPermission("rule:save")
	public String save(Model model, Integer id, Integer[] deviceIds, Integer[] groupIds, String name, String condition, String actionType,
			@RequestParam(defaultValue = "0") Integer status, Integer[] deviceTypes,
			String alarmContent, String alarmType, String alarmLevel, @RequestParam(defaultValue = "0") Integer interval,  
			boolean isFormChanged, boolean includeChildren, boolean isDeviceChanged, boolean isGroupChanged,
			RedirectAttributes redirectAttrs) {
		if (!ugService.isInRootGroup(SecurityUtil.getUserDetail().getUserId())) {
			throw new AccessDeniedException("Chỉ người dùng nhóm cao nhất được phép lưu luật cảnh báo");
		}

		log.info("Saving rule {}, apply to devices {}, deviceTypes {}, groups {}", id, deviceIds, deviceTypes, groupIds);
		if (isFormChanged) {
			if (id != null) {	// update
				//SecurityUtil.authorize("group", "update", group);
				log.debug("Update rule {}", id);
				Rule r = ruleService.findById(id);
				ActionCreateAlarm a = new ActionCreateAlarm(alarmType, alarmContent, alarmLevel);
				r.setName(name);
				r.setCondition(condition);
				r.setAction(JsonUtil.toString(a));
				r.setActionType(actionType);
				r.setStatus(status);
				r.setMinInterval(interval);
				if (deviceTypes != null) {
					RuleMetadata rm = new RuleMetadata();
					for (int dt: deviceTypes)
						rm.getDeviceTypes().add(dt);
					r.setMetadata(JsonUtil.toString(rm));
				} else {
					r.setMetadata(null);
				}
				if (includeChildren)
					r.setScope(Rule.SCOPE_RECURSIVE_GROUP);
				else 
					r.setScope(Rule.SCOPE_NON_RECURSIVE_GROUP);
				ruleService.save(r);
				if (isDeviceChanged)
					ruleService.saveRuleDeviceLink(r, deviceIds);
				if (isGroupChanged)
					ruleService.saveRuleGroupLink(r, groupIds);
				
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("update.success"));
			} else { 	// create new rule
				//SecurityUtil.authorize("group", "create", group);
				ActionCreateAlarm a = new ActionCreateAlarm(alarmType, alarmContent, alarmLevel);
				Rule r = new Rule(null, name, condition, JsonUtil.toString(a), actionType, status, interval);
				if (deviceTypes != null) {
					RuleMetadata rm = new RuleMetadata();
					for (int dt: deviceTypes)
						rm.getDeviceTypes().add(dt);
					r.setMetadata(JsonUtil.toString(rm));
				} else {
					r.setMetadata(null);
				}
				if (includeChildren)
					r.setScope(Rule.SCOPE_RECURSIVE_GROUP);
				else 
					r.setScope(Rule.SCOPE_NON_RECURSIVE_GROUP);
				ruleService.save(r);

				ruleService.saveRuleDeviceLink(r, deviceIds);

				ruleService.saveRuleGroupLink(r, groupIds);
				id = r.getId();
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("create.success"));
			}
			
		} else {
			log.info("No change was made");
		}
		//return "redirect:/rule/view/" + id;
		return "redirect:/rule";
	}
	
	@GetMapping("/delete/{id}")
	@NeedPermission("rule:delete")
	public String delete(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		ruleService.delete(id);
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("delete.success"));
		return "redirect:/rule";
	}
}
