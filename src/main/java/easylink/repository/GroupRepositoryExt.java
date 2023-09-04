package easylink.repository;

import java.util.List;

import easylink.dto.GroupDto;

public interface GroupRepositoryExt {

	List<GroupDto> findGroupDtoByUserId(Integer userid);
	
	List<GroupDto> findAllGroupDto();
}
