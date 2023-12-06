package easylink.repository;

import easylink.entity.Device;
import easylink.entity.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {

    @Query("select count(d.id) from Device d where d.typeId = :typeId")
    Integer countDeviceByType(Integer typeId);

    @Query("select d from Device d where d.typeId = :id")
    List<Device> findDeviceByType(int id);

    @Query("select dt.sensors from DeviceType dt, Device d where d.deviceToken = ?1 and d.typeId = dt.id")
    String findUsedSensorsFromDeviceToken(String deviceToken);
}
