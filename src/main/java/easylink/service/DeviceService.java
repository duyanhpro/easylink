package easylink.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easylink.dto.DeviceListDto;
import easylink.dto.Location;
import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import easylink.repository.DeviceStatusRepository;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	
	public List<Device> findAll() {
		return repo.findAll();
	}

	public List<DeviceListDto> getDeviceList() { return repo.findDeviceListDto(); }

	public List<Device> findAllActive() {
		return repo.findAllByStatus(Device.STATUS_ACTIVE);
	}
	
	public Device findById(int id) {
		return repo.findById(id).orElse(null);
	}

	@Cacheable("device")
	public Device findByToken(String token) {
		return repo.findByDeviceToken(token);
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

	public List<String> findAllTags() {
		return repo.findAllGroup();
	}

	@CacheEvict(cacheNames = "device",key = "#device.deviceToken")
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
			throw new ServiceException("Cập nhật không thành công. Device đã tồn tại!", e);
		}
	}

	public void deleteById(int id) {
		repo.deleteById(id);
	}

	public Device updateLocation(int camId, Location location) {
		log.debug("Update location device ID {}", camId);
		Device cam = findById(camId);
		cam.setLat(location.getLat());
		cam.setLon(location.getLon());
		return repo.save(cam);
	}

}
