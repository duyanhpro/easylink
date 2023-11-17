package easylink.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.Device;
import easylink.security.NeedPermission;
import easylink.service.AlarmService;
import easylink.service.DeviceService;
import easylink.service.StarrocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.vivas.core.util.DateUtil;

@Controller
@RequestMapping("/alarm")
public class AlarmController extends BaseController {

	@Autowired
    AlarmService alarmService;
	@Autowired
	StarrocksService starrocks;
	@Autowired
    DeviceService deviceService;

	// List & Search device
	@NeedPermission("alarm:list")
	@GetMapping("")
	public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "30") Integer pageSize
			,@RequestParam(defaultValue = "-1") Integer selectedDevice, @RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String level, 
			@RequestParam(defaultValue = "") String startDate, @RequestParam(defaultValue = "") String endDate,	 
			@RequestParam(defaultValue = "") String redirect
			) {
		
		log.debug("device {}, type= {}, level= {}, redirect={} , from {} to {}, page {} pageSize {}",
				selectedDevice, type, level, redirect, startDate, endDate, page, pageSize );
		
		model.addAttribute("pageTitle", "Quản lý cảnh báo");
		model.addAttribute("page", page);
		model.addAttribute("pageSize", pageSize);
		
		model.addAttribute("selectedDevice", selectedDevice);
		model.addAttribute("type", type);
		model.addAttribute("level", level);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		Date start, end;
		if (startDate.isEmpty()) start = null;
			else start = DateUtil.string2Date(startDate, "yyyy-MM-dd");
		if (endDate.isEmpty())
			end = null;
		else {
			end = DateUtil.string2Date(endDate, "yyyy-MM-dd");
			end = DateUtil.addInterval(end, 1, TimeUnit.DAYS);
		}
		Device d = deviceService.findById(selectedDevice);
//		Page<Alarm> p = alarmService.findAlarm(selectedDevice<=0? null:selectedDevice, type.isEmpty()? null:type,
//				level.isEmpty()? null: AlarmLevel.valueOf(level), start, end, page, pageSize);
		List<Alarm> la = starrocks.searchAlarm(d==null? null: d.getDeviceToken(), start, end, level.isEmpty()? null: AlarmLevel.valueOf(level).ordinal(),
				type.isEmpty()? null:type, page, pageSize);
		Integer countAllAlarm = starrocks.countAlarm(null, start, end, level.isEmpty()? null: AlarmLevel.valueOf(level).ordinal(),
				type.isEmpty()? null:type);
		model.addAttribute("devices", deviceService.findAllMyDevices());
		model.addAttribute("mypage", new PageImpl<Alarm>(la, PageRequest.of(page-1, pageSize), countAllAlarm)); // start from page 0
		return "alarm/list";
	}
}
