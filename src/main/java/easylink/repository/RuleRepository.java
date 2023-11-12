package easylink.repository;

import java.util.List;

import easylink.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {

	@Query("select v.deviceId from RuleDevice v where v.ruleId = ?1")
	List<Integer> findAppliedDeviceIds(int ruleId);

	@Query("select v.groupId from RuleGroup v where v.ruleId = ?1")
	List<Integer> findAppliedGroupIds(int ruleId);

	@Query("select r from Rule r, RuleDevice rs where rs.deviceToken = ?1 and r.id = rs.ruleId")
	List<Rule> findByDeviceToken(String deviceToken);
	
	@Query("select rs.deviceToken from RuleDevice rs where rs.ruleId = ?1")
	List<String> findDeviceTokenByRule(int ruleId);		// todo:  change token need to update this table!

	List<Rule> findByStatus(int statusActive);

	@Query("delete from RuleDevice rs where rs.ruleId = ?1")
	@Transactional
	@Modifying
	void deleteRuleDeviceLink(Integer id);

	@Query("delete from RuleGroup rs where rs.ruleId = ?1")
	@Transactional
	@Modifying
	void deleteRuleGroupLink(Integer id);
}
