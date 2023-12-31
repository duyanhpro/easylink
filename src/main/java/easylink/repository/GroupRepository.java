package easylink.repository;

import java.util.List;
import java.util.Set;

import easylink.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>, GroupRepositoryExt {

	List<Group> findByNameLike(String groupName);

	@Query("select g from Group g, UserGroup ug"
			+ " where g.id=ug.groupId and ug.userId = :userId"
			+ " order by g.name asc")
	List<Group> findAllByUserIdSortbyName(@Param("userId") int userId);

	List<Group> findAllByOrderByNameAsc();

	@Query("select g from Group g where g.parentId = :groupId")
	List<Group> findChildren(int groupId);

	@Query("select g.id from Group g where g.parentId = :groupId")
	List<Integer> findChildrenIds(int groupId);

	// 12/11 update: fix root node with id = 1
//	@Query("select g from Group g where g.parentId is null or g.parentId = 0")
//	List<Group> findRootNode();

	@Query("select ug.groupId from UserGroup ug where ug.userId = :userId")
	List<Integer> findGroupIdsByUserId(int userId);

//	@Query(nativeQuery = true, value = "select group_id from tbl_user_group where user_id = ?1")
//    List<Integer> findGroupIdAndChildrenByUser(int userId);
}
