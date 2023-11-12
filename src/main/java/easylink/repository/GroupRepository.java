package easylink.repository;

import java.util.List;

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

	@Query("select g from Group g where g.parentId is null or g.parentId = 0")
	List<Group> findRootNode();

	@Query("select g.id from Group g, UserGroup ug where g.id=ug.groupId and ug.userId = :userId")
	List<Integer> findGroupIdsByUserId(int userId);
}
