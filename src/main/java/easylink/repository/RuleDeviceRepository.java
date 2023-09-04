package easylink.repository;

import easylink.entity.RuleDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RuleDeviceRepository extends JpaRepository<RuleDevice, Integer> {

	@Query("delete from RuleDevice rs where rs.ruleId = ?1")
	@Transactional
	@Modifying
	void deleteRuleDeviceLink(Integer id);
}
