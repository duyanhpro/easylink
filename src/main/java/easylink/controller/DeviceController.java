package easylink.controller;

import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.exception.ServiceException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/device")
public class DeviceController extends BaseController {

	@Autowired
	DeviceService deviceService;
	// List & Search device
	@NeedPermission("device:list")
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách trạm");
		model.addAttribute("devices", deviceService.findAll());
		return "device/list";
	}
	
	// view device detail or edit it
	@GetMapping("/edit/{id}")
	@NeedPermission("device:view")
	public String getDevice(Model model, @PathVariable int id) {

		if (id == 0) {
			log.debug("Create new device");
			model.addAttribute("pageTitle", "Tạo mới trạm");
			SecurityUtil.authorize("device", "create");
			
			model.addAttribute("action", "create");
			model.addAttribute("device", new Device());
		} else {
			model.addAttribute("pageTitle", "Cập nhật trạm");
			log.debug("Showing detail of device id {}", id);
			model.addAttribute("action", "update");
			
			Device g = deviceService.findById(id);
			SecurityUtil.authorize("device", "view", g);
			model.addAttribute("device", g);
		}
		model.addAttribute("streets", deviceService.findAllStreet());
		model.addAttribute("districts", deviceService.findAllDistrict());
		model.addAttribute("cities", deviceService.findAllCity());
		model.addAttribute("tags", deviceService.findAllTags());
		model.addAttribute("allGroups", groupService.findAll());
		
		return "device/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("device:save")
	public String save(Model model, @ModelAttribute Device device, RedirectAttributes redirectAttrs) {
		try {
			log.info("Creating/Updating device {}", device);
			if (device.getId()!=null) {
				SecurityUtil.authorize("device", "update", device);
				deviceService.createOrUpdate(device);
				redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật thành công");
			} else { 
				SecurityUtil.authorize("device", "create", device);
				deviceService.createOrUpdate(device);
				redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật thành công");
			}
		} catch (ServiceException e) {
			log.error("Save exception: " + e.getMessage());
			redirectAttrs.addFlashAttribute("errorMsg", e.getMessage());
		}			
		//return "redirect:/device/edit/" + device.getId();
		return "redirect:/device";
	}
	
	// Delete user
	@GetMapping("/delete/{id}")
	@NeedPermission("device:delete")
	public String deletedevice(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Delete device id {}", id);
		SecurityUtil.authorize("device", "delete", deviceService.findById(id));
		deviceService.deleteById(id);
		redirectAttrs.addFlashAttribute("infoMsg", "Xóa thành công");
		return "redirect:/device";
	}
	
	// find status
	@GetMapping("/status/{id}")
	@NeedPermission("device:status")
	public String getStatus(Model model, @PathVariable int id) {
		Device device = deviceService.findById(id);
		DeviceStatus ss = deviceService.findStatus(device.getDeviceToken());
		
		return "redirect:/device/edit/" + device.getId();
	}
}
