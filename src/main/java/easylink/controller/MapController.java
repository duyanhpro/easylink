package easylink.controller;

import easylink.security.NeedPermission;
import easylink.service.ConfigParamService;
import easylink.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MapController extends BaseController {

	@Autowired
    DeviceService deviceService;
	@Autowired
	ConfigParamService configParamService;

	// List & Search device
	@NeedPermission("map:view")
	@GetMapping("map")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Bản đồ trạm");
		model.addAttribute("devices", deviceService.findAllMyDevices());
		model.addAttribute("mapUrl", configParamService.getConfig("MAP_URL"));
		return "map/device-map";
	}

}
