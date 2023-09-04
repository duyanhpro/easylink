package easylink.repository;

import easylink.entity.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface GroupRoleRepository extends JpaRepository<GroupRole, Integer>	{
	@Modifying
	void deleteByGroupId(int groupId);
}
