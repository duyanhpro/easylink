package easylink.repository;

import java.util.List;

import easylink.dto.IUserDto;
import easylink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(nativeQuery = true, 
			value = "select * from tbl_user where status=1 order by username asc")
	List<User> findAllActiveSortByName();

	List<User> findByStatus(int status);
	
	User findByUsername(String username);

//	@Query("select u from User u where username like %?1% or ?1 is null order by username asc")
//	List<User> findByUsernameLike(String username);
	
//	@Query(value = "select u from User u where u.username like ?1 or ?1 is null order by u.username asc",
//			countQuery = "select count(u) from User u where u.username like ?1 or ?1 is null order by u.username asc")
//	Page<User> findByUsernameLike(String username, Pageable page);

//	@Query(nativeQuery = true, value = "select * from tbl_user where " +
//			"id in (select user_id from tbl_user_group where group_id in (select group_id from tbl_user_group where user_id = ?1)) " +
//			"order by username asc")
	@Query(nativeQuery = true, value = "select * from tbl_user " +
			"where id in (select user_id from tbl_user_group " +
			"where group_id in (select group_id from tbl_user_group where user_id = ?1) and inherit = false) " +
		"order by username asc")
	List<User> findAllMyUserOrderByUsernameAsc(int userId);

	@Query(nativeQuery = true, value = "select u.id as id, u.username as username, u.email as email, u.status as status, g.name as 'group', r.name as 'role' from tbl_user u, tbl_group g, tbl_role r, tbl_user_group ug, tbl_user_role ur " +
			"where u.id = ug.user_id and u.id = ur.user_id and g.id = ug.group_id and r.id = ur.role_id and ug.inherit = false " +
			"and u.id in (select user_id from tbl_user_group " +
			" where group_id in (select group_id from tbl_user_group where user_id = ?1) and inherit = false) " +
			" order by u.username asc")
	List<IUserDto> findAllMyUserWithGroupAndRole(int userId);

	void deleteByUsername(String username);

	List<User> findByType(int userType);

	List<User> findAllByOrderByUsernameAsc();

//	  @Query(value = "SELECT * FROM tbl_user WHERE username like %?1% ORDER BY ?#{#pageable}",	// this part ORDER BY ?#{#pageable} is a hack to make it work with MySQL
//	    countQuery = "SELECT count(*) FROM tbl_user WHERE username like %?1%",
//	    nativeQuery = true)
//	  Page<User> findByUsernameWithPagingByNativeQuery(String username, Pageable pageable);
//
//	  @Query(value = "SELECT u from User u WHERE u.username like %?1% order by username asc",
//			    countQuery = "SELECT count(u) FROM User u WHERE u.username like %?1%")
//	Page<User> findByUsernameWithPaging(String username, Pageable pageable);
//	  
//	  
	
}
