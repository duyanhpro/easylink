package easylink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import easylink.entity.RolePermission;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
	@Modifying
	@Query("delete from RolePermission r where r.roleId = ?1")
	public void deleteByRoleId(int roleId);

	public void deleteByRoleIdAndPermissionId(int roleId, int permissionId);
	
	public List<RolePermission> findByPermissionId(int permissionId);
}
