package easylink.repository;

import easylink.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>	{
	@Modifying
	void deleteByUserId(int userId);
}
