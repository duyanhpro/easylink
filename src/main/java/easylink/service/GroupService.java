package easylink.service;

import java.util.List;

import easylink.dto.GroupNode;
import easylink.entity.Group;
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
	
	public List<Group> findAll() {
		return repo.findAllByOrderByNameAsc();
	}
	
	public List<Group> findGroupByUserIdOrderbyGroupName(int userId) {
		return repo.findAllByUserIdSortbyName(userId);
	}

	public List<Group> findByGroupNameSimilar(String groupName) {
		return repo.findByNameLike(groupName);
	}

	public Group findById(int id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Group not found"));
	}

	@Transactional
	public Group update(int groupId, Group group) {
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
}
