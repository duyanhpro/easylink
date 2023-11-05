package easylink.repository;


import java.util.List;

import easylink.dto.DeviceListDto;
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
	
	@Query("select distinct v.tags from Device v where v.tags is not null and v.tags != ''")
	List<String> findAllGroup();

    Device findByDeviceToken(String deviceToken);

    List<Device> findAllByStatus(int status);

	@Query("SELECT new easylink.dto.DeviceListDto(d.id, d.name, d.description, d.location, d.city, g.name, d.deviceToken, d.tags) FROM Device d, Group g WHERE d.groupId = g.id")
	List<DeviceListDto> findDeviceListDto();
}
