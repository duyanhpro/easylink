package easylink.repository;

import easylink.entity.DeviceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, DeviceGroup> {

    @Modifying
    void deleteByDeviceId(Integer id);

    @Query("select dg.deviceId from DeviceGroup dg where dg.groupId = :groupId")
    List<Integer> findAllDeviceIdByGroup(int groupId);

    @Query("select dg.deviceId from DeviceGroup dg where dg.groupId = :groupId and dg.inherit = false")
    List<Integer> findDeviceIdByGroup(int groupId);

    @Modifying
    void deleteByDeviceIdAndGroupId(Integer deviceId, Integer groupId);


}
