package easylink.controller;

import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.exception.ServiceException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.ConfigParamService;
import easylink.service.DeviceService;
import easylink.service.DeviceStatusService;
import easylink.service.DeviceTypeService;
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

	@Autowired
	DeviceStatusService deviceStatusService;

	@Autowired
	DeviceTypeService deviceTypeService;
	@Autowired
	ConfigParamService configParamService;

	// List & Search device
	@NeedPermission("device:list")
	@GetMapping("")
	public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "30") Integer pageSize) {
		model.addAttribute("pageTitle", "Danh sách Trạm");
		//model.addAttribute("devices", deviceService.findAll());
//		model.addAttribute("devices", deviceService.getDeviceList());

//		List<DeviceGroupDto> l = deviceService.findMyDeviceWithGroup(page, pageSize);
//		Long count = deviceService.countMyDevices();
//		model.addAttribute("deviceGroups", deviceService.findMyDeviceWithGroup(page, pageSize));
//		model.addAttribute("mypage", new PageImpl<DeviceGroupDto>(l, PageRequest.of(page-1, pageSize), count)); // start from page 0
		Long start = System.currentTimeMillis();
		model.addAttribute("deviceGroups", deviceService.findMyDeviceWithGroup2());
		System.out.println("Query devices in " + (System.currentTimeMillis() - start));

		return "device/list";
	}
	
	// view device detail or edit it
	@GetMapping("/edit/{id}")
	@NeedPermission("device:view")
	public String getDevice(Model model, @PathVariable int id) {
		model.addAttribute("mapUrl", configParamService.getConfig("MAP_URL"));
		if (id == 0) {
			log.debug("Create new device");
			model.addAttribute("pageTitle", "Tạo mới Trạm");
			SecurityUtil.authorize("device", "create");
			
			model.addAttribute("action", "create");
			model.addAttribute("device", new Device());
		} else {
			model.addAttribute("pageTitle", "Cập nhật Trạm");
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
		model.addAttribute("allGroups", groupService.findAllMyGroups());
		model.addAttribute("deviceTypes", deviceTypeService.findAll());
		
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
			log.error("Save exception: " + e.getMessage(), e);
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
		DeviceStatus ss = deviceStatusService.findStatus(device.getDeviceToken());
		
		return "redirect:/device/edit/" + device.getId();
	}
}
