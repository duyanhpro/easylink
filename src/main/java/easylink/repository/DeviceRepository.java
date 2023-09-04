package easylink.repository;


import java.util.List;

import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

	@Query("select distinct v.street from Device v where v.street is not null and v.street != ''")
	List<String> findAllStreet();
	
	@Query("select distinct v.district from Device v where v.district is not null and v.district  != ''")
	List<String> findAllDistrict();
	
	@Query("select distinct v.city from Device v where v.city is not null and v.city != ''")
	List<String> findAllCity();
	
	@Query("select v.name from Device v")
	List<String> findAllDeviceNames();
	
	@Query("select distinct v.group from Device v where v.group is not null and v.group != ''")
	List<String> findAllGroup();

	@Query("select v from DeviceStatus v where v.deviceToken = :deviceToken")
	DeviceStatus findStatus(@Param("deviceToken") String deviceToken);
}
