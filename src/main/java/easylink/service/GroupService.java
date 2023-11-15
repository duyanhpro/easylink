package easylink.service;

import java.util.*;

import easylink.dto.GroupNode;
import easylink.entity.DeviceGroup;
import easylink.entity.Group;
import easylink.entity.User;
import easylink.exception.AccessDeniedException;
import easylink.repository.DeviceGroupRepository;
import easylink.repository.UserGroupRepository;
import easylink.security.SecurityUtil;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.exception.NotFoundException;
import easylink.repository.GroupRepository;

@Service
public class GroupService {

	static Logger log = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	private GroupRepository repo;
	@Autowired
	private UserGroupRepository ugRepo;
	@Autowired
	private UserGroupService ugService;
	@Autowired
	DeviceGroupRepository dgRepo;
	@Autowired
	DeviceService deviceService;
	
	public Set<Group> findAllMyGroups() {
		return findGroupAndChilrenGroup(SecurityUtil.getUserDetail().getUserId());
		//return repo.findAllByOrderByNameAsc();
	}
	
	public List<Group> findGroupByUserId(int userId) {
		return repo.findAllByUserIdSortbyName(userId);
	}
	public Set<Integer> findGroupIdAndChildrenByUser(int userId) {
		List<Integer> lg = repo.findGroupIdsByUserId(userId);
		Set<Integer> a = new HashSet<>();

		a.addAll(lg);
		for (Integer groupId: lg) {
			findGroupIdAndChildren(groupId, a);
		}
		return a;
	}
	private void findGroupIdAndChildren(int groupId, Set<Integer> s) {	// recursively
			List<Integer> li = repo.findChildrenIds(groupId);
			s.addAll(li);
			for (Integer g: li) {
				findGroupIdAndChildren(g, s);
			}
	}

	/**
	 * Find all ID of parent nodes (not include current group id)
	 * @param groupId
	 * @return
	 */
	public Set<Integer> findAllParentIds(Integer groupId) {
		Set<Integer> temp = new TreeSet<>();
		findAllParentIdsRecursively(groupId, temp);
		return temp;
	}
	private void findAllParentIdsRecursively(Integer groupId, Set<Integer> result) {
		if (groupId == 1) return;		// root group, return empty Set
		Group g = findById(groupId);
		result.add(g.getParentId());
		findAllParentIdsRecursively(g.getParentId(), result);
	}

//	public List<Group> findByGroupNameSimilar(String groupName) {
//		return repo.findByNameLike(groupName);
//	}

	public Group findById(int id) {
		// Check permission to view this group
		Set<Integer> myGroup = findGroupIdAndChildrenByUser(SecurityUtil.getUserDetail().getUserId());
		if (myGroup.contains(id))
			return repo.findById(id).orElseThrow(() -> new NotFoundException("Group not found"));
		else
			throw new AccessDeniedException("Can not view this group");
	}

	@Transactional
	public Group update(int groupId, Group group) {
		// Check permission
		Set<Integer> myGroup = findGroupIdAndChildrenByUser(SecurityUtil.getUserDetail().getUserId());
		if (!myGroup.contains(groupId))
			throw new AccessDeniedException("Can not update this group");

		Group g = repo.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
		if (g.getParentId() != group.getParentId()) {
			// Update group structure relationship
			updateGroupTree(groupId, g.getParentId(), group.getParentId());
		}
		BeanUtil.merge(g, group);
		return repo.save(g);
	}

	@Transactional
	public Group create(Group group) {
		Group g = repo.saveAndFlush(group);
		log.info("Created new group: {}", g);
		updateGroupTree(g.getId(), null, g.getParentId());
		return g;
	}

	/**
	 * Update user_group table & device_group table to reflect new group hierachy when parent change
	 * @param groupId
	 * @param oldParentId:  null when create new group
	 * @param newParentId
	 */
	public void updateGroupTreeWip(int groupId, Integer oldParentId, Integer newParentId) {

		// Truong hop tao moi group.  Chua co group con, chua co device nao -> easy
		// --> user-group bo sung them link toi cac user cua nut cha;  device-group khong thay doi


		// Truong hop sua group, chuyen sang new parent
		// device-group:  old-parent & cac grandparent:   xoa link toi cac device cua nhom nay
		//				  new-parent & cac grandparent:  them link toi device cua group nay (all including inherit)
		// user-group:    old-parent & grandparent:  tim cac user, xoa link toi group nay va cac group con
		//				  new parent & grandparent:  tim cac user cua parent, them link toi group nay va cac group con

		// 1. add device-group relationship for all parents:
		// devices belong directly or to children of this group will have relationship with new parents (including grantParents)
		List<Integer> devices = dgRepo.findAllDeviceIdByGroup(groupId);	// this group's devices, including inherit
		// Remove link of those devices with all old parents
		if (oldParentId != null) {
			Set<Integer> oldParentIds = findAllParentIds(oldParentId);
			oldParentIds.add(oldParentId);
			for (Integer i : devices) {
				for (Integer p : oldParentIds) {
					dgRepo.deleteByDeviceIdAndGroupId(i, p);
				}
			}
		}
		// Add link to new parents
		Set<Integer> newParentIds = findAllParentIds(newParentId);
		newParentIds.add(newParentId);
		for (Integer i: devices) {
			for (Integer p: newParentIds)
				dgRepo.save(new DeviceGroup(i, p, true));
		}

		// TODO: 2. Update user-group relationship
		// User of old parent ID and its parent:  remove link with this group
		// User of new parent ID and all its parent:  add link to this group
		// OR:  find list 1 all oldParentIds, find list 2 all new ParentIds.
		//  remove which is in list1, not in list 2.  Add which is in list2, except what already in list 1

		// for new parent ID, remove inherited link to oldParentId
		// Note:  user-group link could be from many different group (not just this group!) --> how to know which to remove
		// --> rebuild entire table???
		// users belong directly to this group and grand-parent will have relationship with all children group
		List<Integer> userIds = ugRepo.findAllUserIdByGroup(groupId);
		// remove user-group link of oldParentId and all parents of it

	}

	public void updateGroupTree(int groupId, Integer oldParentId, Integer newParentId) {
		// lam kieu tho thien nhat
		ugService.rebuildUserGroupRelationship();
		deviceService.rebuildAllDeviceGroupRelationship();
	}

	@Transactional
	public void deleteById(int id) {
		repo.deleteById(id);
	}

	@Transactional
	public void deleteAndUpdateTree(int id) {
		// todo: Find user or devices in this group. If exist then not allow delete
		List<User> users = ugRepo.findAllUserByGroupIdNoInherit(id);
		if (!users.isEmpty()) throw new RuntimeException("Không thể xóa. Có người dùng thuộc nhóm này!");
		List<Integer> devices = dgRepo.findDeviceIdByGroup(id);
		if (!devices.isEmpty()) throw new RuntimeException("Không thể xóa. Có thiết bị thuộc nhóm này!");

		// Check permission
		Set<Integer> myGroup = findGroupIdAndChildrenByUser(SecurityUtil.getUserDetail().getUserId());
		if (!myGroup.contains(id))
			throw new AccessDeniedException("Can not update this group");

		log.info("Delete a node and update all child nodes to point to grandfather");
		Group g =  findById(id);
		List<Group> children = repo.findChildren(id);
		for (Group gg: children) {
			gg.setParentId(g.getParentId());
			repo.save(gg);
		}
		repo.delete(g);
	}

	/**
	 * Used for JS lib to dislay tree in Group page
	 * @return
	 */
	public GroupNode getGroupNodeTree() {
		//GroupNode root = new GroupNode("root", "Tất cả");
		Group g = repo.findById(1).get();    // fix root node have id = 1
//		for (Group g: roots) {
		GroupNode root = new GroupNode(String.valueOf(g.getId()), g.getName());
		//root.getChildren().add(n);
		getChildrenRecursively(root);
//		}
		return root;
	}

	private void getChildrenRecursively(GroupNode n) {
		List<Group> lg = repo.findChildren(Integer.parseInt(n.getId()));
		for (Group g: lg) {
			GroupNode child = new GroupNode(String.valueOf(g.getId()), g.getName());
			n.getChildren().add(child);
			getChildrenRecursively(child);
		}
	}

	public Set<Group> findGroupAndChilrenGroup(int userId) {
		List<Group> lg = repo.findAllByUserIdSortbyName(userId);		// có thể bị lặp
		Set<Group> a = new TreeSet<>();
		a.addAll(lg);
		for (Group g: lg) {
			findChildrenRecursively(g.getId(), a);
		}
		return a;
	}

	private void findChildrenRecursively(int groupId, Set<Group> lg) {
			List<Group> children = repo.findChildren(groupId);
			lg.addAll(children);
			for (Group g: children) {
				findChildrenRecursively(g.getId(), lg);
			}
	}

	/**
	 * Find ids of all group and their children. Including input
	 * @param groupIds
	 * @return
	 */
	public Set<Integer> findAllGroupIds(Integer[] groupIds) {
		Set<Integer> temp = new TreeSet<>();
		for (Integer i: groupIds) {
			temp.add(i);
			findAllGroupIds(i, temp);
		}
		return temp;
	}
	private void findAllGroupIds(int groupId, Set<Integer> groupIds) {
		List<Integer> children = repo.findChildrenIds(groupId);
		if (children == null || children.isEmpty()) return;
		groupIds.addAll(children);
		for (int i: children)
			findAllGroupIds(i, groupIds);
	}
}
