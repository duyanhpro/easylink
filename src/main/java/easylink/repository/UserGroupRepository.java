package easylink.repository;

import java.util.List;

import easylink.entity.Group;
import easylink.entity.User;
import easylink.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer>	{

	@Modifying
	void deleteByUserId(Integer id);

	@Query("select g from Group g, UserGroup ug where g.id = ug.groupId and ug.userId = ?1 and ug.inherit = false")
	List<Group> findByUserIdNoInherit(int userId);
	
	@Query("select u from User u, UserGroup ug where u.id = ug.userId and ug.groupId = ?1 and ug.inherit = false")
	List<User> findAllUserByGroupIdNoInherit(int groupId);
	
	@Query("select u.username from User u, UserGroup ug where u.id=ug.userId and ug.groupId= :id and ug.inherit = false")
	List<String> findUsernameByGroupIdNoInherit(@Param("id") int id);
	
	@Modifying
	void deleteByGroupId(Integer groupId);
	
	@Query(value = "select g.name from tbl_group g, tbl_user_group ug where g.id=ug.group_id and ug.inherit = 0 "
			+ "and ug.user_id = ?1", nativeQuery = true)
	List<String> findGroupNameByUserId(int userId);

	@Query("select g from Group g, UserGroup ug where ug.userId = ?1 and ug.groupId = g.id")
	List<Group> findAllGroupByUserId(Integer userId);

	@Query("select g from Group g, UserGroup ug where ug.userId = ?1 and ug.groupId = g.id and ug.inherit = false")
	List<Group> findGroupByUserIdNoInherit(Integer userId);		// only 1 user

	@Query("select ug.userId from UserGroup ug where ug.groupId = :groupId")
	List<Integer> findAllUserIdByGroup(int groupId);	// include inherit

	@Query("select ug.userId from UserGroup ug where ug.groupId = :groupId and ug.inherit = true")
	List<Integer> findInheritedUserIdByGroup(int groupId);	// include inherit
}
