package easylink.controller;

import easylink.entity.Device;
import easylink.entity.DeviceType;
import easylink.exception.ServiceException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/dtype")
public class DeviceTypeController extends BaseController {

	@Autowired
	DeviceTypeService deviceTypeService;

	// List & Search
	@NeedPermission("device:list")
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách loại thiết bị");
		model.addAttribute("deviceTypes", deviceTypeService.findAll());
		return "device-type/list";
	}
	
	// view detail or edit it
	@GetMapping("/edit/{id}")
	@NeedPermission("device:view")
	public String getDeviceType(Model model, @PathVariable int id) {

		if (id == 0) {
			log.debug("Create new device type");
			model.addAttribute("pageTitle", "Tạo mới loại thiết bị");

			model.addAttribute("action", "create");
			model.addAttribute("deviceType", new DeviceType());
			model.addAttribute("devices", new ArrayList<Device>());
		} else {
			model.addAttribute("pageTitle", "Cập nhật loại thiết bị");
			log.debug("Showing detail of deviceType id {}", id);
			model.addAttribute("action", "update");
			
			DeviceType g = deviceTypeService.findById(id);
			model.addAttribute("devices", deviceTypeService.findDeviceByType(id));
			//SecurityUtil.authorize("device", "view", g);
			model.addAttribute("deviceType", g);
		}

		return "device-type/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("device:save")
	public String save(Model model, @ModelAttribute DeviceType deviceType, RedirectAttributes redirectAttrs) {
		try {
			log.info("Creating/Updating device type {}", deviceType);
			if (deviceType.getId()!=null) {
				//SecurityUtil.authorize("device", "update", device);
				deviceTypeService.save(deviceType);
				redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật thành công");
			} else { 
				//SecurityUtil.authorize("device", "create", device);
				deviceTypeService.save(deviceType);
				redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật thành công");
			}
		} catch (ServiceException e) {
			log.error("Save exception: " + e.getMessage(), e);
			redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
		}			
		return "redirect:/dtype/edit/" + deviceType.getId();
		//return "redirect:/dtype";
	}
	
	// Delete
	@GetMapping("/delete/{id}")
	@NeedPermission("device:delete")
	public String deletedevice(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Delete device id {}", id);

		deviceTypeService.deleteById(id);
		redirectAttrs.addFlashAttribute("infoMsg", "Xóa thành công");
		return "redirect:/dtype";
	}
}
