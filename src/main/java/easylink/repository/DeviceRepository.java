package easylink.repository;


import java.util.List;

import easylink.dto.DeviceListDto;
import easylink.dto.IDeviceWithGroupDto;
import easylink.entity.Device;
import easylink.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

//	@Query("SELECT new easylink.dto.DeviceListDto(d.id, d.name, d.description, d.location, d.city, g.name, d.deviceToken, d.tags) FROM Device d, Group g WHERE d.groupId = g.id")
//	List<DeviceListDto> findDeviceListDto();

	//@Procedure("GetDevicesForUser") // only work with simple return procedure
	//@Query(value = "CALL GetDevicesForUser(:userId);", nativeQuery = true)
	@Query(nativeQuery = true, value = "select * from tbl_device where id in (select device_id from tbl_device_group where group_id in " +
			"(select group_id from tbl_user_group where user_id = ?1)) order by id asc")
	List<Device> getDevicesForUser(@Param("userId") Integer userId);	// get devices of groups of user (recursively)

	@Query(nativeQuery = true, value = "select d.id, d.name, d.location, d.city, g.name as groupName, dt.name as deviceTypeName from tbl_device d, tbl_group g, tbl_device_type dt where " +
			"d.group_id = g.id and d.type_id = dt.id and d.id in (select device_id from tbl_device_group where group_id in " +
			"(select group_id from tbl_user_group where user_id = ?1)) order by id asc")
	List<IDeviceWithGroupDto> getDevicesForUserWithGroup(@Param("userId") Integer userId);

	@Query(nativeQuery = true, value = "select count(device_id) from tbl_device_group where group_id in " +
			"(select group_id from tbl_user_group where user_id = ?1)")
	Long countDevicesForUser(@Param("userId") Integer userId);	// get devices of groups of user (recursively)

	//@Query(value = "CALL GetDeviceIdsForUser(:userId);", nativeQuery = true)
	@Query(nativeQuery = true, value = "select device_id from tbl_device_group where group_id in " +
			"(select group_id from tbl_user_group where user_id = ?1) order by id asc")
	List<Integer> getDeviceIdsForUser(@Param("userId") Integer userId);	// get device IDs of groups of user (recursively)

	@Query(nativeQuery = true, value = "select * from tbl_device where id in (select device_id from tbl_device_group where group_id in " +
			"(select group_id from tbl_user_group where user_id = ?1)) order by id asc limit ?3 offset ?2")
	List<Device> getDevicesForUserWithPaging(int userId, int offset, int limit);

	@Modifying
	@Query("delete from DeviceGroup dg where dg.deviceId = ?1")
	void deleteDeviceGroupByDeviceId(int deviceId);

	@Query("delete from RuleDevice rs where rs.deviceId = ?1")
	@Modifying
	void deleteRuleDeviceLinkByDeviceId(Integer deviceId);
}
