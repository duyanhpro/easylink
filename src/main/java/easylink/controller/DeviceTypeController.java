package easylink.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.entity.Device;
import easylink.entity.DeviceType;
import easylink.exception.ServiceException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DeviceSchemaService;
import easylink.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.vivas.core.util.JsonUtil;

import java.util.ArrayList;

@Controller
@RequestMapping("/dtype")
public class DeviceTypeController extends BaseController {

	@Autowired
	DeviceTypeService deviceTypeService;
	@Autowired
	DeviceSchemaService deviceSchemaService;

	// List & Search
	@NeedPermission("device:list")
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách loại trạm");
		model.addAttribute("deviceTypes", deviceTypeService.findAll());
		return "device-type/list";
	}
	
	// view detail or edit it
	@GetMapping("/edit/{id}")
	@NeedPermission("device:view")
	public String getDeviceType(Model model, @PathVariable int id) throws JsonProcessingException {

		model.addAttribute("allFields", deviceSchemaService.getAllFields(1));
		if (id == 0) {
			log.debug("Create new device type");
			model.addAttribute("pageTitle", "Tạo mới loại trạm");

			model.addAttribute("action", "create");
			model.addAttribute("deviceType", new DeviceType());
			model.addAttribute("devices", new ArrayList<Device>());
			model.addAttribute("myFields", null);
		} else {
			model.addAttribute("pageTitle", "Cập nhật loại trạm");
			log.debug("Showing detail of deviceType id {}", id);
			model.addAttribute("action", "update");
			
			DeviceType g = deviceTypeService.findById(id);
			model.addAttribute("devices", deviceTypeService.findDeviceByType(id));
			model.addAttribute("deviceType", g);

			ObjectMapper objectMapper = new ObjectMapper();
			model.addAttribute("myFields", g.getSensors());
		}

		return "device-type/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("device:save")
	public String save(Model model, @ModelAttribute DeviceType deviceType, String[] selectedFields, RedirectAttributes redirectAttrs) {
		try {
			log.info("Creating/Updating device type {}, selectedFields {}", deviceType, selectedFields);

			if (selectedFields == null || selectedFields.length == 0)
				deviceType.setSensors(null);
			else {
				deviceType.setSensors(String.join(",", selectedFields));
			}
			if (deviceType.getId()!=null) {
				//SecurityUtil.authorize("device", "update", device);
				deviceTypeService.save(deviceType);
				redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật thành công");
			} else { 
				//SecurityUtil.authorize("device", "create", device);
				deviceTypeService.save(deviceType);
				redirectAttrs.addFlashAttribute("infoMsg", "Tạo mới thành công");
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
