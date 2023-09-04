package easylink.service;

import java.util.List;

import easylink.entity.Group;
import easylink.entity.User;
import easylink.entity.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.repository.UserGroupRepository;

@Service
public class UserGroupService {
	
	static Logger log = LoggerFactory.getLogger(UserGroupService.class);
	
	@Autowired
	UserGroupRepository userRepo;
	
	@Transactional
	public void updateGroupForUser(User user, Integer[] groupIds) {
		if (groupIds == null)
			userRepo.deleteByUserId(user.getId());
		else {
			userRepo.deleteByUserId(user.getId());	
			userRepo.flush();			// Must do this or delete in a separate transaction, else will error when save
			for (Integer gid: groupIds) {
				userRepo.save(new UserGroup(user.getId(), gid));
			}
		}
	}
	
	public List<Group> findGroupByUserId(int userId) {
		return userRepo.findByUserId( userId);
	}
	
	public List<String> findGroupNameByUserId(int userId) {
		return userRepo.findGroupNameByUserId( userId);
	}

	public List<String> findUserNameByGroupId(int id) {
		return userRepo.findUsernameByGroupId(id);
	}
	
	public List<User> findAllUserByGroupId(int groupId) {
		return userRepo.findAllUserByGroupId(groupId);
	}

	@Transactional
	public void updateUserForGroup(Group group, Integer[] userIds) {
		if (userIds == null) {
			userRepo.deleteByGroupId(group.getId());
		} else {
			userRepo.deleteByGroupId(group.getId());
			userRepo.flush();
			for (Integer userId: userIds) {
				userRepo.save(new UserGroup(userId, group.getId()));
			}
		}
	}
}
