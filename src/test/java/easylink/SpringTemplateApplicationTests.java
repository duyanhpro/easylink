package easylink;

import java.util.Date;

import easylink.service.DeviceStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import easylink.entity.DeviceStatus;
import easylink.service.DeviceService;

@SpringBootTest
class SpringTemplateApplicationTests {

	@Autowired
	DeviceService deviceService;
	@Autowired
	DeviceStatusService deviceStatusService;
	
	@Test
	void contextLoads() {
//			DeviceStatus st = deviceStatusService.findStatus("tram4-hanoi");
//			DeviceStatus st2 = new DeviceStatus();
//			st2.setDeviceToken(deviceService.findById(1).getDeviceToken());
//			st2.setEventTime(new Date());
//			st2.setTelemetry(st.getTelemetry());
//			st2.setStatus(st.getStatus());
//			deviceStatusService.updateStatus(st2);
//
//			st2.setDeviceToken(deviceService.findById(2).getDeviceToken());
//			st2.setEventTime(new Date());
//			st2.setTelemetry(st.getTelemetry());
//			st2.setStatus(st.getStatus());
//			deviceStatusService.updateStatus(st2);
//
//			st2.setDeviceToken(deviceService.findById(3).getDeviceToken());
//			st2.setEventTime(new Date());
//			st2.setTelemetry(st.getTelemetry());
//			st2.setStatus(st.getStatus());
//			deviceStatusService.updateStatus(st2);
	}

}
