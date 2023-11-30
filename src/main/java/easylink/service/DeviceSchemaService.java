package easylink.service;

import easylink.entity.Device;
import easylink.entity.DeviceSchema;
import easylink.repository.DeviceSchemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeviceSchemaService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DeviceSchemaRepository repo;

    @Autowired
    DeviceService deviceService;

    @Cacheable("deviceSchema")
    public DeviceSchema findById(int id) {
        return repo.findById(id).get(); // do not use getOne, it's lazy operation
    }

    public List<DeviceSchema> findAll() { return repo.findAll(); }

    @Cacheable("deviceSchemaByToken")
    public DeviceSchema findFromDeviceToken(String deviceToken) {
        Device d = deviceService.findByToken(deviceToken);
        //log.debug("Device d: " + d);
        if (d == null) return null;
        return findById(d.getSchemaId());
    }

    @CacheEvict(cacheNames = "deviceType", key = "#newObj.id")
    public void updateDeviceSchema(DeviceSchema newObj) {
        //TODO
    }
//    public Map<String, String> getSchemaFromToken(String deviceToken) {
//        DeviceSchema dt = findFromDeviceToken(deviceToken);
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
