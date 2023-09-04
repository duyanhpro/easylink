package easylink.service;

import java.util.List;

import easylink.entity.Group;
import easylink.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.exception.NotFoundException;
import easylink.repository.GroupRepository;

@Service
public class GroupService {
	
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
}
