package easylink.repository;

import java.util.List;

import easylink.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

	@Query("select p from Permission p, RolePermission pr where pr.roleId = ?1 and p.id = pr.permissionId")
	List<Permission> findByRoleId(Integer id);

	List<Permission> findByResourceAndAction(String resource, String action);
	
	Permission findByResourceAndActionAndConditionAndAllow(String resource, String action, String condition, Boolean allow);

	@Query("select p from Permission p where p.id = ?1")
	Permission findOne(Integer id);
	
	@Query("select p from Permission p where p.resource=?1 and p.action=?2 and (?3=null or p.condition=?3) and p.allow=?4")
	Permission findOne(String resource, String action, String condition, boolean allow);
	
	
}
