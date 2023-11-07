package easylink.repository;


import easylink.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Integer> {

	@Query("select v from DeviceStatus v where v.deviceToken = :deviceToken")
	DeviceStatus findStatus(@Param("deviceToken") String deviceToken);

	@Query("select s.deviceToken from DeviceStatus s, Device d where s.deviceToken = d.deviceToken and " +
			"s.status = :status and d.status = 1")
	List<String> findTokenByStatus(int status);

	@Modifying
	@Query("update DeviceStatus s SET s.status = :status WHERE s.deviceToken = :token")
	void updateStatus(String token, int status);

	@Query("select v from DeviceStatus v, Device d where v.deviceToken = d.deviceToken and d.status = 1")
    List<DeviceStatus> findActiveDevices();
}
