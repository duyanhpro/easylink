package easylink.repository;

import java.util.Date;

import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
