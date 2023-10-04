package easylink.repository;

import easylink.entity.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {
    public List<DeviceType> findByStatus(int status);
}
