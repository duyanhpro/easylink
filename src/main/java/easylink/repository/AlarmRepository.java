package easylink.repository;

import java.util.Date;
import java.util.List;

import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.DeviceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {

	Page<Alarm> findAll(Pageable page);
	
	// Select only violation (no alpr), with paging
	@Query(value = "SELECT v from Alarm v WHERE "
			+ "(:type is null or :type = v.type) AND "
			+ "(:level is null or :level = v.level) AND "
			+ "(:deviceToken is null or :deviceToken = v.deviceToken) AND "
			+ "(:start is null or :start <= v.eventTime) AND "
			+ "(:end is null or :end > v.eventTime) "
			+ " order by v.eventTime desc",
			    countQuery = "SELECT count(v) from Alarm v WHERE "
						+ "(:type is null or :type = v.type) AND "
						+ "(:level is null or :level = v.level) AND "
						+ "(:deviceToken is null or :deviceToken = v.deviceToken) AND "
						+ "(:start is null or :start <= v.eventTime) AND "
						+ "(:end is null or :end > v.eventTime)")
	Page<Alarm> findAlarm(@Param("deviceToken") String deviceToken, @Param("type") String type, @Param("level") AlarmLevel level,
			@Param("start") Date start, @Param("end") Date end, Pageable page);

	@Query("select v from Alarm v where :deviceToken = v.deviceToken and (:start is null or :start <= v.eventTime) order by v.eventTime desc")
	List<Alarm> findRecentAlarm(@Param("deviceToken") String deviceToken, @Param("start") Date start, Pageable pageable);

	@Query(value = "CALL GetAlarmsForUser(:userId);", nativeQuery = true)
	List<Alarm> getAlarmsForUser(@Param("userId") Integer userId);	// get alarms of devices of groups of user (recursively)
}
