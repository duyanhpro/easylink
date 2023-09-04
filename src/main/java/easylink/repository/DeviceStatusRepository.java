package easylink.repository;


import easylink.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Integer> {

	@Query("select v from DeviceStatus v where v.deviceToken = :deviceToken")
	DeviceStatus findStatus(@Param("deviceToken") String deviceToken);
}
