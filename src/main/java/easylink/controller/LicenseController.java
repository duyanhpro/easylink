package easylink.controller;

import easylink.dto.AlarmLevel;
import easylink.dto.LicenseInfo;
import easylink.entity.Alarm;
import easylink.entity.Device;
import easylink.entity.Group;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.vivas.core.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/license")
public class LicenseController extends BaseController {

	@Autowired
	LicenseService licenseService;
	@Autowired
    DeviceService deviceService;

	// List & Search device
	@NeedPermission("license:view")
	@GetMapping("")
	public String list(Model model) throws Exception {
		LicenseInfo l = licenseService.getLicense();
		//model.addAttribute("lic", licenseService.getLicense());
		model.addAttribute("pageTitle", "License phần mềm");
		model.addAttribute("lic", l);
		model.addAttribute("currentDeviceCount", deviceService.countDevice());
		model.addAttribute("licenseKey", licenseService.getLicenseKey());
		//model.addAttribute("isExpired", l==null? true: (l.getExpireDate()==null? false: (l.getExpireDate().getTime() < new Date().getTime() ? true: false)));
		String status = "Hợp lệ";
		if (l == null) status = "Không có license";
		else {
			if (l.getExpireDate() != null) {
				if (l.getExpireDate().getTime() < new Date().getTime())
					status = "Đã hết hạn";
			}
			if (deviceService.countDevice() >= l.getMaxDevice())
				status = "Đã dùng hết số lượng trạm được phép";
		}
		model.addAttribute("licenseStatus", status);

		return "license";	// go to license.html template
	}

	@NeedPermission("license:update")
	@PostMapping("")
	public String update(Model model, String licenseKey, RedirectAttributes redirectAttrs) {
		log.info("Update license key ");
		if (licenseService.updateLicenseKey(licenseKey))
			redirectAttrs.addFlashAttribute("infoMsg", "Cập nhật license thành công");
		else
			redirectAttrs.addFlashAttribute("errorMsg", "License không hợp lệ");
		return "redirect:/license";
	}


}
