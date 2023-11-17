package easylink.service;

import java.util.*;

import easylink.dto.DeviceGroupDto;
import easylink.dto.DeviceListDto;
import easylink.dto.Location;
import easylink.entity.Device;
import easylink.entity.DeviceGroup;
import easylink.entity.Group;
import easylink.repository.DeviceGroupRepository;
import easylink.repository.GroupRepository;
import easylink.security.SecurityUtil;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	GroupService groupService;
	@Autowired
	GroupRepository groupRepo;
	@Autowired
	DeviceGroupRepository dgRepo;

	// Find all belong to this user
	public List<DeviceGroupDto> findMyDeviceWithGroup() {
		List<DeviceGroupDto> ldg = new ArrayList<>();
		List<Device> ld = findAllMyDevices();
		List<Group> lg = groupRepo.findAll();
		Map<Integer, Group> mg = new HashMap<>();
		for (Group g: lg) {
			mg.put(g.getId(), g);
		}
		for (Device d: ld) {
			DeviceGroupDto dg = new DeviceGroupDto();
			dg.setDevice(d);
			dg.setGroup(mg.get(d.getGroupId())); // todo:  set to root group if group = null
			ldg.add(dg);
		}
		return ldg;
	}

	/**
	 * Find all devices that user is permitted to see
	 * @return
	 */
	public List<Device> findAllMyDevices() {
//		List<Group> lg = groupService.findGroupByUserId(SecurityUtil.getUserDetail().getUserId());
//		if (lg.isEmpty())
//			return repo.findAll();
		return repo.getDevicesForUser(SecurityUtil.getUserDetail().getUserId());
	}

	public List<Device> findAllDevicesByUserId(int userId) {		// mainly for debug
		return repo.getDevicesForUser(userId);
	}
//	public List<DeviceListDto> getDeviceList() {
//		return repo.findDeviceListDto();
//	}

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
//		try {
			if (device.getId() == null) {    // new device
				Device d = repo.save(device);
				updateDeviceGroupRelationship(device);
				return d;
			} else {
				Device c = repo.findById(device.getId()).orElseThrow(() -> new NotFoundException(""));
				boolean needUpdateGroup = false;
				if (c.getGroupId() != device.getGroupId())
					needUpdateGroup = true;
				BeanUtil.merge(c, device);
				c = repo.save(c);
				if (needUpdateGroup) updateDeviceGroupRelationship(c);
				return c;
			}
//		} catch (Exception e) {
//			throw new Exception("Cập nhật không thành công: " , e);
////			throw new ServiceException("Cập nhật không thành công: " + e.getMessage(), e);
//		}
	}
	@Transactional
	public void updateDeviceGroupRelationship(Device device) {
		log.debug("Save relationship device {} - group {}", device.getId(), device.getGroupId());
		dgRepo.deleteByDeviceId(device.getId());
		dgRepo.save(new DeviceGroup(device.getId(), device.getGroupId(), false));
		Set<Integer> parentIds = groupService.findAllParentIds(device.getGroupId());
		for (int i: parentIds) {
			log.debug("Save inherited relationship device {} - parent group {}", device.getId(), i);
			dgRepo.save(new DeviceGroup(device.getId(), i, true));
		}
	}
	public void rebuildAllDeviceGroupRelationship() {
		log.info("Rebuild all device-group relationship table");
		for (Device d: repo.findAll()) {
			updateDeviceGroupRelationship(d);
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
