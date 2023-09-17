package easylink.controller.api;

import java.util.List;

import easylink.dto.Location;
import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.exception.AccessDeniedException;
import easylink.security.NeedPermission;
import easylink.service.MqttService;
import easylink.service.DeviceService;
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
    MqttService mqttService;

	@GetMapping("/api/devices")
	@ResponseBody
	@NeedPermission("device:list")
	public List<Device> getListDevice() {
		return deviceService.findAll();
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
		return deviceService.findStatus(deviceToken);
	}
	
	@GetMapping("/api/devices/status")
	@ResponseBody
	@NeedPermission("device:list")
	public List<DeviceStatus> getAllDeviceStatus() {
		return deviceService.findAllStatus();
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
}
