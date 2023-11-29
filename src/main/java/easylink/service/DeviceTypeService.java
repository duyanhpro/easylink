package easylink.service;

import easylink.entity.Device;
import easylink.entity.DeviceType;
import easylink.repository.DeviceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceTypeService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    DeviceTypeRepository repo;

    public List<DeviceType> findAll() {
        return repo.findAll();
    }
    public DeviceType findById(int id) {
        return repo.findById(id).get();
    }
    public List<Device> findDeviceByType(int id) {
        log.trace("Find device by type: " + repo.findDeviceByType(id));
        return repo.findDeviceByType(id);
    }

    public DeviceType save(DeviceType d) {
        return repo.save(d);
    }
    public void delete(DeviceType d) {
        // check if any device is using this type
        Integer count = repo.countDeviceByType(d.getId());
        if (count > 0)
            throw new RuntimeException("Không thể xóa vì có trạm thuộc loại này. Vui lòng chuyển hết trạm sang loại khác trước khi xóa");
        // delete if allowed
        repo.delete(d);
    }

    public void deleteById(int id) {
        // Check quyen. Khong cho xoa neu co thiet bi thuoc loai nay
        Integer count = repo.countDeviceByType(id);
        if (count > 0)
            throw new RuntimeException("Không thể xóa vì có trạm thuộc loại này. Vui lòng chuyển hết trạm sang loại khác trước khi xóa");
        repo.deleteById(id);
    }
}
