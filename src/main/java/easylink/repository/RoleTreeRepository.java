package easylink.repository;

import java.util.List;

import easylink.entity.Role;
import easylink.entity.RoleTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTreeRepository extends JpaRepository<RoleTree, Integer> {
	
	@Query("select r from Role r, RoleTree rt where rt.parentId = r.id and rt.childId = ?1")
	List<Role> findParentRoles(int roleId);
	
	@Modifying
	@Query("delete from RoleTree rt where rt.childId = ?1")
	void removeParentRoles(int roleId);

	void deleteByChildIdAndParentId(int childId, int parentId);

	void deleteByChildId(int roleId);
}
