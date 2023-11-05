package easylink.controller;

import easylink.dto.ActionCreateAlarm;
import easylink.entity.Rule;
import easylink.security.NeedPermission;
import easylink.service.RuleService;
import easylink.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vivas.core.util.JsonUtil;

@Controller
@RequestMapping("/rule")
public class RuleController extends BaseController {

	@Autowired
    RuleService ruleService;
	
	@Autowired
	DeviceService deviceService;
	
	// List rule
	@GetMapping("")
	@NeedPermission("rule:list")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Quản lý luật cảnh báo");
		model.addAttribute("rules", ruleService.findAll());
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
		return "rule/edit";
	}
	
	// Save rule (insert or update)
	@PostMapping("/save")
	@NeedPermission("rule:save")
	public String save(Model model, Integer id, Integer[] deviceIds, String name, String condition, String actionType, 
			@RequestParam(defaultValue = "0") Integer status, 
			String alarmContent, String alarmType, String alarmLevel, @RequestParam(defaultValue = "0") Integer interval,  
			boolean isFormChanged, boolean allDevice, boolean isDeviceChanged,
			RedirectAttributes redirectAttrs) {
		log.info("Saving rule {}, apply to devices {}, allDevice={}", id, deviceIds, allDevice);
		if (isFormChanged) {
			if (id != null) {
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
				if (allDevice) 
					r.setScope(Rule.SCOPE_ALL_DEVICES);
				else 
					r.setScope(Rule.SCOPE_PER_DEVICE);
				ruleService.save(r);
				if (!allDevice && isDeviceChanged)
					ruleService.saveRuleDeviceLink(r, deviceIds);
				
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("update.success"));
			} else { 
				//SecurityUtil.authorize("group", "create", group);
				ActionCreateAlarm a = new ActionCreateAlarm(alarmType, alarmContent, alarmLevel);
				Rule r = new Rule(null, name, condition, JsonUtil.toString(a), actionType, status, interval);
				if (allDevice) 
					r.setScope(Rule.SCOPE_ALL_DEVICES);
				else 
					r.setScope(Rule.SCOPE_PER_DEVICE);
				ruleService.save(r);
				if (!allDevice && isDeviceChanged)
					ruleService.saveRuleDeviceLink(r, deviceIds);
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
