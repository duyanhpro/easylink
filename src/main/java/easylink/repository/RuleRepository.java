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
	
	@Query("select d.deviceToken from RuleDevice rs, Device d where rs.ruleId = ?1 and rs.deviceId = d.id")
	List<String> findDeviceTokenByRule(int ruleId);

	@Query("select d.deviceToken from RuleGroup rg, DeviceGroup dg, Device d where rg.ruleId = ?1 and rg.groupId = dg.groupId and dg.deviceId = d.id")
	List<String> findDeviceTokenByRuleFromGroup(int ruleId);

	@Query("select d.deviceToken from RuleGroup rg, DeviceGroup dg, Device d where rg.ruleId = ?1 and rg.groupId = dg.groupId and dg.inherit = false and dg.deviceId = d.id")
	List<String> findDeviceTokenByRuleFromGroupNoRecursive(int ruleId);

	List<Rule> findByStatus(int statusActive);

	@Query("select ug.groupId from UserGroup ug, Rule r where r.id = :ruleId and ug.userId = r.createdUser and ug.inherit = false")
	Integer findCreatedUserGroup(int ruleId);

	@Query("delete from RuleDevice rs where rs.ruleId = ?1")
	@Transactional
	@Modifying
	void deleteRuleDeviceLink(Integer id);

	@Query("delete from RuleGroup rs where rs.ruleId = ?1")
	@Transactional
	@Modifying
	void deleteRuleGroupLink(Integer id);
}
