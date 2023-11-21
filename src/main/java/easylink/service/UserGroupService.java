package easylink.service;

import java.util.List;
import java.util.Set;

import easylink.entity.DeviceGroup;
import easylink.entity.Group;
import easylink.entity.User;
import easylink.entity.UserGroup;
import easylink.repository.DeviceGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.repository.UserGroupRepository;

@Service
@Transactional
public class UserGroupService {
	
	static Logger log = LoggerFactory.getLogger(UserGroupService.class);
	
	@Autowired
	UserGroupRepository ugRepo;
	@Autowired
	GroupService groupService;
	@Autowired
	DeviceGroupRepository dgRepo;
	@Autowired
	UserService userService;

	// 15.11.23:  change to each user only in one group, but don't change method. This method tested and worked with n-1
	@Transactional
	public void updateGroupForUser(User user, Integer[] groupIds) {
		log.debug("Update group for user {} with group {}", user.getUsername(), groupIds);
		if (groupIds == null)
			ugRepo.deleteByUserId(user.getId());
		else {
			ugRepo.deleteByUserId(user.getId());
			ugRepo.flush();			// Must do this or delete in a separate transaction, else will error when save
			// Now find all children group and save the relationship
			Set<Integer> groupIdsAndChildren = groupService.findAllGroupIds(groupIds);
			log.debug("Find group ids & children: {}", groupIdsAndChildren);
			for (Integer gid: groupIdsAndChildren) {
				boolean found = false;
				for (Integer i: groupIds) {
					if (i == gid) {	// have direct relationship
						ugRepo.save(new UserGroup(user.getId(), gid, false));
						found = true;
						break;
					}
				}
				if (!found)
					ugRepo.save(new UserGroup(user.getId(), gid, true));
			}
		}
	}

	public void rebuildUserGroupRelationship() {
		log.info("Rebuild user-group relationship table");
		List<User> ud = userService.findAll();
		for (User u: ud) {
			List<Group> lg = findGroupByUserIdNoInherit(u.getId());
			if (lg.isEmpty()) continue;
			Integer [] gid = {lg.get(0).getId()};
			updateGroupForUser(u, gid);
		}
		// todo: missign new group that have no user (but upper user can still manage)
	}

	// find groups directly link to this user
	public List<Group> findGroupByUserIdNoInherit(int userId) {
		return ugRepo.findByUserIdNoInherit( userId);
	}

	public boolean isInRootGroup(int userId) {
		List<Group> lg = ugRepo.findByUserIdNoInherit(userId);
		for (Group g: lg) {
			if (g.getId() == 1) return true;
		}
		return false;
	}
	
	public List<String> findGroupNameByUserId(int userId) {
		return ugRepo.findGroupNameByUserId( userId);
	}

	public List<String> findUserNameByGroupId(int id) {
		return ugRepo.findUsernameByGroupIdNoInherit(id);
	}
	
	public List<User> findAllUserByGroupId(int groupId) {
		return ugRepo.findAllUserByGroupIdNoInherit(groupId);
	}

	@Transactional
	public void updateUserForGroup(Group group, Integer[] userIds) {
//		Integer[] groupIds = {group.getId()};
//		List<Integer> childrenGroup = groupService.findAllGroupIds(groupIds);
		if (userIds == null) {
//			ugRepo.deleteByGroupId(group.getId());	// not allow, or device will point to no group
		} else {
			ugRepo.deleteByGroupId(group.getId());
			ugRepo.flush();
			for (Integer userId: userIds) {
				ugRepo.save(new UserGroup(userId, group.getId()));
				// todo:  add link inherit between userId and all child group
			}
		}
	}
}
