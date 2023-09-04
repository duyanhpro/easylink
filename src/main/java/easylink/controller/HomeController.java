package easylink.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	static Logger log = LoggerFactory.getLogger(HomeController.class);

	// Open home page
	@GetMapping("/")
	public String root(Model model) {
//		if (SecurityUtil.getUserDetail().getUser().getType()== User.TYPE_ADMIN)
			return "redirect:/dashboard";
//		else
//			return "redirect:/mydashboard";
	}
		
	// Open home page
	@GetMapping("/home")
	public String home(Model model) {
		return "redirect:/dashboard";
	}
	
	// Just open login page then let Spring Security handle the rest
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
//	// Open home page
//		@GetMapping("/theme/{page}")
//		public String testTheme(Model model, @PathVariable String page) {
//			return page;
//		}
	
	
}
