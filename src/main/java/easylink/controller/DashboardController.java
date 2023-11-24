package easylink.controller;

import easylink.dto.ActionCreateAlarm;
import easylink.entity.Dashboard;
import easylink.entity.Group;
import easylink.entity.Rule;
import easylink.exception.AccessDeniedException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DashboardService;
import easylink.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.vivas.core.util.JsonUtil;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	UserGroupService ugService;

	@GetMapping("/main")
	@NeedPermission("mainDashboard")
	public String homeDashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard tổng hợp");
		model.addAttribute("homeDashboard", configService.getHomeDashboardUrl()
				+ "&var-user_id=" + SecurityUtil.getUserDetail().getUserId());
		return "dashboard/dashboard";
	}
	
	@GetMapping("/device")
	@NeedPermission("dashboard:device")
	public String deviceDashboard(Model model) {
		model.addAttribute("pageTitle", "Giám sát thiết bị");
		model.addAttribute("deviceDashboard", configService.getDeviceDashboardUrl()
				+ "&var-user_id=" + SecurityUtil.getUserDetail().getUserId());
		return "dashboard/device";
	}

	@GetMapping("/list")
	@NeedPermission("dashboard:list")
	public String listDashboard(Model model) {
		model.addAttribute("pageTitle", "Danh sách Dashboard");
		model.addAttribute("dashboards", dashboardService.findAll());
		model.addAttribute("isRoot",   ugService.isInRootGroup(SecurityUtil.getUserDetail().getUserId()));
		return "dashboard/list";
	}
	@GetMapping("/delete/{id}")
	@NeedPermission("dashboard:delete")
	public String delete(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		dashboardService.delete(id);
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("delete.success"));
		return "redirect:/dashboard/list";
	}

	@GetMapping("/edit/{id}")
	@NeedPermission("dashboard:edit")
	public String editDashboard(Model model, @PathVariable int id) {
		model.addAttribute("pageTitle", "Chi tiết Dashboard");
		model.addAttribute("action", "update");
		model.addAttribute("dashboard", dashboardService.findById(id));
		model.addAttribute("isRoot", SecurityUtil.hasRole("ADMIN") && ugService.isInRootGroup(SecurityUtil.getUserDetail().getUserId()));
		return "dashboard/edit";
	}

	@GetMapping("/view/{id}")
	@NeedPermission("dashboard:view")
	// View dashboard
	public String getDashboard(Model model, @PathVariable int id) {
		model.addAttribute("pageTitle", "Chi tiết Dashboard");
		String url = dashboardService.findById(id).getUrl();
		if (!url.contains("user_id")) {
			if (!url.contains("?"))
				url += "?force=1&standalone=2&user_id=" + SecurityUtil.getUserDetail().getUserId();
			else
				url+="&force=1&standalone=2&user_id=" + SecurityUtil.getUserDetail().getUserId();
		}
		model.addAttribute("dashboardUrl", url);
		return "dashboard/view";
	}

	// create new rule
	@NeedPermission("dashboard:create")
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pageTitle", "Tạo mới Dashboard");
		model.addAttribute("action", "create");
		model.addAttribute("dashboard", new Dashboard());
		model.addAttribute("isRoot", SecurityUtil.hasRole("ADMIN") && ugService.isInRootGroup(SecurityUtil.getUserDetail().getUserId()));
		return "dashboard/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("dashboard:update")
	public String save(Model model, @ModelAttribute Dashboard dashboard,  RedirectAttributes redirectAttrs) {
		log.info("Creating/Updating dashboard {}", dashboard);

		if (dashboard.getId() == null) {
			dashboardService.save(dashboard);
			redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));

		} else {
			// update
			dashboardService.update(dashboard.getId(), dashboard);
			redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));
		}
		return "redirect:/dashboard/list";
	}

}
