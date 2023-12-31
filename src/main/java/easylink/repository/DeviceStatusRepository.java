package easylink.repository;


import easylink.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Integer> {

	@Query("select v from DeviceStatus v where v.deviceToken = :deviceToken")
	DeviceStatus findStatus(@Param("deviceToken") String deviceToken);

	@Query("select s.deviceToken from DeviceStatus s, Device d where s.deviceToken = d.deviceToken and " +
			"s.status = :status and d.status = 1")
	List<String> findActiveDeviceTokenByDeviceStatus(int status);

	@Modifying
	@Query("update DeviceStatus s SET s.status = :status WHERE s.deviceToken = :token")
	@Transactional		// hmm somehow try this because exception in production
	void updateStatus(String token, int status);

	@Query("select v from DeviceStatus v, Device d where v.deviceToken = d.deviceToken and d.status = 1")
    List<DeviceStatus> findActiveDevices();

	@Query(value = "CALL GetDeviceStatusForUser(:userId);", nativeQuery = true)
	List<DeviceStatus> getDeviceStatusForUser(@Param("userId") Integer userId);	// get device statuses of groups of user (recursively)

	@Modifying
	@Query("delete from DeviceStatus s where s.deviceToken = ?1")
	void deleteStatus(String token);
}
