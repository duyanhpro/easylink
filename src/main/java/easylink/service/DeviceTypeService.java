package easylink.service;

import easylink.entity.DeviceType;
import easylink.repository.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceTypeService {
    @Autowired
    DeviceTypeRepository repo;

    public List<DeviceType> findAll() {
        return repo.findAll();
    }
    public DeviceType findById(int id) {
        return repo.findById(id).get();
    }
    public DeviceType save(DeviceType d) {
        return repo.save(d);
    }
    public void delete(DeviceType d) {
        // check if any device is using this type
        Integer count = repo.countDeviceByType(d.getId());
        if (count > 0)
            throw new RuntimeException("Không thể xóa vì có thiết bị thuộc loại này. Vui lòng chuyển hết thiết bị sang loại khác trước khi xóa");
        // delete if allowed
        repo.delete(d);
    }
}
