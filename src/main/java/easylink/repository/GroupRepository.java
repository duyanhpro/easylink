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
			+ " where g.id=ug.groupId and ug.userId = :userid"
			+ " order by g.name asc")
	List<Group> findAllByUserIdSortbyName(@Param("userid") int userid);

	List<Group> findAllByOrderByNameAsc();
}
