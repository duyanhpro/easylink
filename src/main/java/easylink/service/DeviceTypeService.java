package easylink.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.entity.Device;
import easylink.entity.DeviceType;
import easylink.repository.DeviceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceTypeService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DeviceTypeRepository repo;

    @Autowired
    DeviceService deviceService;

    @Cacheable("deviceType")
    public DeviceType findById(int id) {
        return repo.findById(id).get(); // do not use getOne, it's lazy operation
    }

    public List<DeviceType> findAll() { return repo.findAll(); }

    public List<DeviceType> findActive() {
        return repo.findByStatus(DeviceType.STATUS_ACTIVE);
    }

    @Cacheable("deviceTypeByToken")
    public DeviceType findFromDeviceToken(String deviceToken) {
        // TODO: should get from cache for better performance
        Device d = deviceService.findByToken(deviceToken);
        //log.debug("Device d: " + d);
        if (d == null) return null;
        return findById(d.getDeviceTypeId());
    }

    @CacheEvict(cacheNames = "deviceType", key = "#newObj.id")
    public void updateDeviceType(DeviceType newObj) {
        //TODO
    }
//    public Map<String, String> getSchemaFromToken(String deviceToken) {
//        DeviceType dt = findFromDeviceToken(deviceToken);
//        if (dt == null) return null;
//        // convert JSON string to Map
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Map<String, String> map = mapper.readValue(dt.getDataSchema(), Map.class);
//            log.debug("Find schema from device token {}, schema: {}", deviceToken, map);
//            return map;
//        } catch (Exception e) {
//            log.error("Exception when getting schema: {}", e);    // should not happen! should validate schema before save
//            return null;
//        }
//    }
}
