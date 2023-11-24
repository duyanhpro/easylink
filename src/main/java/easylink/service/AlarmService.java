package easylink.service;

import java.util.Date;
import java.util.List;

import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import easylink.repository.AlarmRepository;
import easylink.repository.DeviceRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.vivas.core.util.JsonUtil;

@Service
@Transactional
public class AlarmService {
	
//	@Autowired
//	AlarmRepository alarmRepo;
	
//	@Autowired
//    DeviceRepository deviceRepo;

	@Autowired
	MqttService mqttService;

	@Autowired
	DeviceService deviceService;

	@Autowired
	StarrocksService starrocks;

	@Value("${mqtt.topic.alarm}")
	String alarmTopic;

	Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Alarm> findAlarm(Integer deviceId, String type, AlarmLevel level,
                                 Date start, Date end, int page, int pageSize) {

		Device d = deviceService.findById(deviceId);
//		Page<Alarm> p = alarmService.findAlarm(selectedDevice<=0? null:selectedDevice, type.isEmpty()? null:type,
//				level.isEmpty()? null: AlarmLevel.valueOf(level), start, end, page, pageSize);
		List<Alarm> la = starrocks.searchAlarm(d==null? null: d.getDeviceToken(), start, end, level==null? null: level.ordinal(),
				type.isEmpty()? null:type, page, pageSize);
		Integer countAllAlarm = starrocks.countAlarm(null, start, end, level == null? null: level.ordinal(),
				type.isEmpty()? null:type);

		return new PageImpl<Alarm>(la, PageRequest.of(page-1, pageSize), countAllAlarm);
//		String deviceToken = null;
//		if (deviceId != null)
//			deviceToken = deviceRepo.findById(deviceId).get().getDeviceToken();
//		return alarmRepo.findAlarm(deviceToken, type, level, startTime, endTime, PageRequest.of(pageNumber-1, pageSize));
	}
	
//	public Page<Alarm> findAll(int pageNumber, int pageSize) {
//		return alarmRepo.findAll(PageRequest.of(pageNumber-1, pageSize));
//	}

	public void createAlarm(Alarm a) {
		// TODO:  create queue to save alarm in batch
		// send alarm to MQTT
		log.debug("Create alarm {}", a);
		mqttService.sendToMqtt(alarmTopic + "/" + a.getDeviceToken(), JsonUtil.toString(a));
		// save alarm
		saveAlarm(a);
	}

	// Save alarm generated by rule
	@Transactional
	public void saveAlarm(Alarm a) {
		//alarmRepo.save(a);
		starrocks.insertAlarm(a);
	}

	/**
	 * get alarm from last numberOfDay, limit maximum number of record returned
	 * @param deviceToken
	 * @param numberOfDay
	 * @param limit
	 * @return
	 */
	public List<Alarm> getRecentAlarm(String deviceToken, int numberOfDay, int limit) {
		log.debug("Get recent alarm in last {} days, limit {} records", numberOfDay, limit);
		// Get the current time in milliseconds
		long currentTimeMillis = System.currentTimeMillis();

		// Calculate the time in milliseconds for the desired number of days ago
		long daysInMillis = numberOfDay * 24 * 60 * 60 * 1000L; // 1 day = 24 hours * 60 minutes * 60 seconds * 1000 milliseconds
		long timeInMillisAgo = currentTimeMillis - daysInMillis;
		//log.debug("Loading data from " + new Date(timeInMillisAgo));
		// Create a Date object with the calculated time and return it
		return starrocks.searchAlarm(deviceToken, new Date(timeInMillisAgo), new Date(), null, null, 1, limit);
		//return alarmRepo.findRecentAlarm(deviceToken, new Date(timeInMillisAgo), PageRequest.of(0, limit));
	}
}
