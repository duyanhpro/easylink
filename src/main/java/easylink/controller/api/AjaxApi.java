package easylink.controller.api;

import java.util.List;

import easylink.dto.GroupNode;
import easylink.dto.Location;
import easylink.entity.Alarm;
import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.exception.AccessDeniedException;
import easylink.security.NeedPermission;
import easylink.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AjaxApi {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	DashboardService dashService;
//	
//	@GetMapping("/api/dashboard/{id}")
//	public Dashboard getDashboard(@PathVariable int id) {
//		log.debug("ajax get dashboard " + id);
//		return dashService.findById(id);
//	}

	@Autowired
	DeviceService deviceService;
	@Autowired
	DeviceStatusService deviceStatusService;
	@Autowired
    MqttService mqttService;

	@Autowired
	GroupService groupService;
	@Autowired
	UserGroupService ugService;
	@Autowired
	AlarmService alarmService;

	@Autowired
	RuleService ruleService;

	@GetMapping("/api/devices")
	@ResponseBody
	@NeedPermission("device:list")
	public List<Device> getListDevice() {
		return deviceService.findAllMyDevices();
	}
	
	@PostMapping("/api/devices/{id}/location")
	@ResponseBody
	@NeedPermission("device:save")
	public Device updateLocation(@PathVariable("id") Integer camId, @RequestBody Location location) {
		log.info("Update location for deviceID {} with {}", camId, location);
		return deviceService.updateLocation(camId, location);
	}
	
	@GetMapping("/api/devices/{deviceToken}/status")
	@ResponseBody
	@NeedPermission("device:view")
	public DeviceStatus getStatusDevice(@PathVariable("deviceToken") String deviceToken) {
		return deviceStatusService.findStatus(deviceToken);
	}

	@GetMapping("/api/devices/{deviceToken}/alarm")
	@ResponseBody
	@NeedPermission("device:view")
	public List<Alarm> getDeviceRecentAlarm(@PathVariable("deviceToken") String deviceToken, @RequestParam(defaultValue = "5000") int day, @RequestParam(defaultValue = "30") int limit) {
		log.debug("Get alarm of {} in last {} days, limit {} records", deviceToken, day, limit);
		return alarmService.getRecentAlarm(deviceToken, day, limit);
	}

	@GetMapping("/api/devices/status")
	@ResponseBody
	@NeedPermission("device:list")
	public List<DeviceStatus> getAllDeviceStatus() {
		return deviceStatusService.findStatusOfActiveDevices();
	}

	@GetMapping("/api/devices/rebuildGroupRelationship")		// debug only!
	@ResponseBody
	public String rebuildDeviceGroup() {
		deviceService.rebuildAllDeviceGroupRelationship();
		return "OK";
	}
	@GetMapping("/api/users/rebuildGroupRelationship")		// debug only!
	@ResponseBody
	public String rebuildUserGroup() {
		ugService.rebuildUserGroupRelationship();
		return "OK";
	}

	// Enable SSH tunnel
	@PostMapping("/api/devices/rpc/{deviceToken}")
	@NeedPermission("device:rpc")
	public String sendRpcCommand(@PathVariable String deviceToken,  @RequestBody String body) {
		log.info("Receive RPC command: {}", body);
		mqttService.sendRpcRequest(deviceToken, body);
		String response = null;
		response = mqttService.waitForRpcResponse(deviceToken);
		if (response == null)
			return "Timeout. No response from device.";
		else
			return response;
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(
			Exception ex) {
		return new ResponseEntity<Object>(
				"Access Denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@GetMapping("/api/group/tree")
	@ResponseBody
	public GroupNode getGroupTree() {
		return groupService.getGroupNodeTree();
	}

	@GetMapping("/api/rule/validate")
	@ResponseBody
	public String validate(@RequestParam String condition) {
		return ruleService.validate(condition) ? "OK" : "NOK";
	}
}
