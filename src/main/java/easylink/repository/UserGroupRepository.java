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

	@Query("select g from Group g, UserGroup ug where g.id = ug.groupId and ug.userId = ?1")
	List<Group> findByUserId(int userId);
	
	@Query("select u from User u, UserGroup ug where u.id = ug.userId and ug.groupId = ?1")
	List<User> findAllUserByGroupId(int groupId);
	
	@Query("select u.username from User u, UserGroup ug where u.id=ug.userId and ug.groupId= :id")
	List<String> findUsernameByGroupId(@Param("id") int id);
	
	@Modifying
	void deleteByGroupId(Integer groupId);
	
	@Query(value = "select g.name from tbl_group g, tbl_user_group ug where g.id=ug.group_id "
			+ "and ug.user_id = ?1", nativeQuery = true)
	List<String> findGroupNameByUserId(int userId);

	@Query("select g from Group g, UserGroup ug where ug.userId = ?1 and ug.groupId = g.id")
	List<Group> findAllGroupByUserId(Integer userId);
}
