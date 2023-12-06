package easylink.repository;

import java.util.List;

import easylink.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	List<Role> findByStatus(int status);
	
	@Query("select r from Role r, RoleTree t where r.id = t.childId and t.parentId = ?1")
	List<Role> findChildRoles(int roleId);
	
	@Query("select r from Role r, RoleTree t where r.id = t.parentId and t.childId = ?1")
	List<Role> findParentRoles(int roleId);

	@Query("select r.name from Role r, RoleTree t where r.id = t.parentId and t.childId = ?1")
	List<String> findParentRoleNames(int roleId);

	@Query("select r from Role r, UserRole gr where gr.roleId = r.id and gr.userId = ?1")
	List<Role> findRolesOfUser(int userId);

	@Query("select r from Role r where r.id = ?1")
	Role findOne(Integer id);

	@Query("select r.displayName from Role r, UserRole ur where r.id = ur.roleId and ur.userId = ?1")
	List<String> findDirectRoleNameOfUser(int userId);

	Role findByName(String string);
	
	@Query("select r from Role r, RolePermission gr where gr.roleId = r.id and gr.permissionId = ?1")
	List<Role> findRoleByPermissionId(int permissionId);
}
