package easylink.controller;

import easylink.security.NeedPermission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController extends BaseController {

	@GetMapping("/dashboard")
	@NeedPermission("mainDashboard")
	public String homeDashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard tổng hợp");
		model.addAttribute("homeDashboard", configService.getHomeDashboardUrl());
		return "dashboard/dashboard";
	}
	
	@GetMapping("/deviceDashboard")
	@NeedPermission("device:dashboard")
	public String deviceDashboard(Model model) {
		model.addAttribute("pageTitle", "Giám sát trạm");
		model.addAttribute("deviceDashboard", configService.getDeviceDashboardUrl());
		return "dashboard/device";
	}
}
