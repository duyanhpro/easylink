package easylink.service;

import java.util.*;

import easylink.dto.GroupNode;
import easylink.entity.Group;
import easylink.exception.AccessDeniedException;
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
	public void findGroupIdAndChildren(int groupId, Set<Integer> s) {	// recursively
			List<Integer> li = repo.findChildrenIds(groupId);
			s.addAll(li);
			for (Integer g: li) {
				findGroupIdAndChildren(g, s);
			}
	}
//	public List<Group> findByGroupNameSimilar(String groupName) {
//		return repo.findByNameLike(groupName);
//	}

	public Group findById(int id) {
		Set<Integer> myGroup = findGroupIdAndChildrenByUser(SecurityUtil.getUserDetail().getUserId());
		if (myGroup.contains(id))
			return repo.findById(id).orElseThrow(() -> new NotFoundException("Group not found"));
		else
			throw new AccessDeniedException("Can not view this group");
	}

	@Transactional
	public Group update(int groupId, Group group) {
		Set<Integer> myGroup = findGroupIdAndChildrenByUser(SecurityUtil.getUserDetail().getUserId());
		if (!myGroup.contains(groupId))
			throw new AccessDeniedException("Can not update this group");
		Group g = repo.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
		BeanUtil.merge(g, group);
		return repo.save(g);
	}

	@Transactional
	public Group create(Group group) {
		return repo.save(group);
	}
	
	@Transactional
	public void deleteById(int id) {
		repo.deleteById(id);
	}

	@Transactional
	public void deleteAndUpdateTree(int id) {
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

	public GroupNode getGroupNodeTree() {
		GroupNode root = new GroupNode("root", "Tất cả");
		List<Group> roots = repo.findRootNode();
		for (Group g: roots) {
			GroupNode n = new GroupNode(String.valueOf(g.getId()), g.getName());
			root.getChildren().add(n);
			getChildrenRecursively(n);
		}
		return root;
	}

	public void getChildrenRecursively(GroupNode n) {
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
}
