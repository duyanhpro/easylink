package easylink.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easylink.dto.Location;
import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.repository.DeviceStatusRepository;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import easylink.exception.NotFoundException;
import easylink.exception.ServiceException;
import easylink.repository.DeviceRepository;

@Service
@Transactional
public class DeviceService {
	
	static Logger log = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	DeviceRepository repo;
	
	@Autowired
	DeviceStatusRepository statusRepo;
	
	public List<Device> findAll() {
		return repo.findAll();
	}
	
	public Device findById(int id) {
		return repo.findById(id).orElse(null);
	}

	public List<String> findAllStreet() {
		log.debug(repo.findAllStreet().toString());
		return repo.findAllStreet();
	}

	public List<String> findAllDistrict() {
		return repo.findAllDistrict();
	}
	
	public List<String> findAllCity() {
		return repo.findAllCity();
	}

	public List<String> findAllGroup() {
		return repo.findAllGroup();
	}
	
	public Device createOrUpdate(Device device) {
		log.debug("Saving device: {}", device);
		try {
			if (device.getId() == null)
				return repo.saveAndFlush(device);
			else {
				Device c = repo.findById(device.getId()).orElseThrow(() -> new NotFoundException(""));
				BeanUtil.merge(c, device);
				c = repo.saveAndFlush(c);
				return c;
			}
		} catch (Exception e) {
			throw new ServiceException("Cập nhật không thành công. Device ID đã tồn tại!", e);
		}
	}

	/**
	 * Find status. Convert json to hashmap to display on screen
	 * @param deviceToken
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	public DeviceStatus findStatus(String deviceToken)  {
		DeviceStatus ss = repo.findStatus(deviceToken);
		System.out.println(ss);
		if (ss == null) {
			return new DeviceStatus(deviceToken);
		}
		ObjectMapper mapper = new ObjectMapper();

		// convert JSON string to Map
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = mapper.readValue(ss.getTelemetry(), Map.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// it works
		// 		String json = "{\"name\":\"mkyong\", \"age\":\"37\"}";
		//Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

		log.debug("Loaded status:  {}, status = {}", map, ss.getStatus());
		ss.setTelemetryMap(map);

		return ss;
	}
	
	/**
	 * Update device status when receive event from MQTT
	 * @param deviceToken
	 * @param map
	 * @param msg
	 */
	public void updateDeviceStatus(String deviceToken, Map<String, Object> map, String msg) {
		// TODO: handle it in batch, in a queue 
		// TODO 2: use Redis to store it
		try {
			DeviceStatus st = new DeviceStatus();
			st.setDeviceToken(deviceToken);
			st.setEventTime(new Date());
			st.setTelemetry(msg);
			//st.setStatus((Integer)map.get("status")==1?1:0);		// status:  connection status from gateway to monitoring device
			st.setStatus((Integer)map.get("status"));
			statusRepo.save(st);
		} catch (Exception e) {
			log.error("Exception when updateDeviceStatus: {}", e.getMessage());
		}
	}
	
	public DeviceStatus updateStatus(DeviceStatus st) {
		return statusRepo.save(st);
	}
	
	public void deleteById(int id) {
		repo.deleteById(id);
	}

	public Device updateLocation(int camId, Location location) {
		Device cam = findById(camId);
		cam.setLat(location.getLat());
		cam.setLon(location.getLon());
		return repo.save(cam);
	}

	public List<DeviceStatus> findAllStatus() {
		return statusRepo.findAll();
	}
	
}
