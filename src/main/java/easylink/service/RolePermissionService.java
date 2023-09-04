package easylink.service;

import java.util.*;

import easylink.repository.RolePermissionRepository;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.entity.Group;
import easylink.entity.GroupRole;
import easylink.entity.Permission;
import easylink.entity.Role;
import easylink.entity.RolePermission;
import easylink.entity.RoleTree;
import easylink.entity.UserRole;
import easylink.repository.GroupRoleRepository;
import easylink.repository.PermissionRepository;
import easylink.repository.RoleRepository;
import easylink.repository.RoleTreeRepository;
import easylink.repository.UserGroupRepository;
import easylink.repository.UserRoleRepository;

@Service
@Transactional
public class RolePermissionService {
	
	static Logger log = LoggerFactory.getLogger(RolePermissionService.class);
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	PermissionRepository permRepo;
	
	@Autowired
    RolePermissionRepository rpRepo;

	@Autowired
	RoleTreeRepository rtRepo;
	
	@Autowired
	UserGroupRepository ugRepo;
	
	@Autowired
	GroupRoleRepository grRepo;
	
	@Autowired
	UserRoleRepository urRepo;
	
	/**
	 * Load all roles, then aggregate permissions from all roles
	 * @param userId
	 * @return
	 */
	public Set<Permission> findAllPermissionOfUser(int userId) {
		// First, find all role of user
		Set<Role> sr = findAllRoleOfUser(userId);
		
		// Then, iterate all roles and aggregate permission
		return findAllPermissionOfRoles(sr);
	}
	
	/**
	 * Find all permissions of a set of role
	 */
	private Set<Permission> findAllPermissionOfRoles(Set<Role> sr) {
		Set<Permission> ls = new HashSet<>();		
		
		Iterator<Role> it = sr.iterator();
		while (it.hasNext()) {
			Role r = it.next();
			List<Permission> lp = permRepo.findByRoleId(r.getId());
			
			ls.addAll(lp);
		}
		return ls;
	}

	/**
	 * Find all active roles that this user has (its own role & inherit from all group)
	 * Iterate role tree to add inherited roles
	 * 
	 * @param userId
	 * @return
	 */
	public Set<Role> findAllRoleOfUser(int userId) {
//		log.debug("Find all roles of user (from every source)");
		List<Role> ur = findDirectRoleOfUser(userId);
//		System.out.println("Direct role: " + ur);
		Set<Role> sr = findUserRoleInheritFromGroup(userId);
//		System.out.println("Group role: " + sr);
		sr.addAll(ur);
		
		Set<Role> inheritedRoles = new HashSet<>();
		for (Role r: sr) {
			// Now add role and all parent roles (recursively) into _sr
			findRoleAndParentRole(inheritedRoles, r);
		}
//		System.out.println("Interited role from parent: " + inheritedRoles);
		sr.addAll(inheritedRoles);
		
		return sr;
	}

	/**
	 * Find all roles that user inherits from groups that user belongs to 
	 * @param userId
	 * @return
	 */
	public Set<Role> findUserRoleInheritFromGroup(int userId) {
		List<Group> lg =  ugRepo.findByUserId(userId);
		Set<Role> sr = new HashSet<>();
		for (Group g: lg) {
			sr.addAll(findRoleOfGroup(g));
		}
		return sr;
	}
	
	/**
	 * Find assigned role of a group (direct roles only) 
	 * @param g
	 * @return
	 */
	public List<Role> findRoleOfGroup(Group g) {
		List<Role> lrg = findRoleOfGroup(g.getId());
		return lrg;
	}
	
	/**
	 * Find all parent roles of Role r and add all to Role Set sr
	 //* Return immediately if r already in role set sr (to avoid endless loop)
	 * @param sr
	 * @param r
	 */
	protected void findRoleAndParentRole(Set<Role> sr, Role r) {
		//if (sr.contains(r)) return;	// incorrect because different instance --> different object hash code
		for (Role _r: sr) {
			if (_r.getId() == r.getId())		// meet a role loop --> stop checking 
				return;
		}
		sr.add(r);
		
		List<Role> parents = rtRepo.findParentRoles(r.getId());
		if (parents.isEmpty()) 
			return;
		else {
			for (Role rr: parents) {
				findRoleAndParentRole(sr, rr);
			}
		}
	}
	
	
	public List<Role> listAllRole() {
		return roleRepo.findAll();
	}
	
	public List<Permission> listAllPermission() {
		return permRepo.findAll();
	}
	
	@Transactional
	public Role saveOrUpdateRole(Role r) {
		if (r.getId()==null)
			return roleRepo.save(r);
		else {
			Role old = roleRepo.findOne(r.getId());
			BeanUtil.merge(old, r);
			return roleRepo.save(old);
		}
	}
	
	@Transactional
	public Role createNewRole(Role r) {
		if (r.getId()!=null)
			r.setId(null);
		return roleRepo.save(r);
	}
	
	@Transactional
	public Permission saveOrUpdatePermission(Permission p) {
		if (p.getId() == null)
			return permRepo.save(p);
		else {
			Permission old = permRepo.findOne(p.getId());
			BeanUtil.merge(old, p);
			return permRepo.save(old);
		}
	}
	
	public Permission insertPermission(String resource, String action, String condition, String description, boolean allow) {
		log.debug("Saving permission: resource=" + resource + " - action=" + action + " - condition=" + condition + " - allow=" + allow);
		Permission p = permRepo.findByResourceAndActionAndConditionAndAllow(resource, action, condition, allow);
		if (p != null) {
			log.debug("Permission already exist");
			return p;
		}
		p = new Permission(resource, action, condition, description, allow);
		return saveOrUpdatePermission(p);
	}
	
	@Transactional
	public void deleteRole(int roleId) {
		// Check if this role is not used by any Groups or Users before delete
		// --> this is done by the DB. No need to code :)
		roleRepo.deleteById(roleId);
		
		// Delete relation in roleTree where childId = roleId
		rtRepo.deleteByChildId(roleId);
		
		// Delete relation in rolePermission where roleId
		rpRepo.deleteByRoleId(roleId);
	}
	
	@Transactional
	public void deletePermission(int permissionId) {
		permRepo.deleteById(permissionId);
	}

	public Role findRoleById(int id) {
		return roleRepo.findOne(id);
	}

	public List<Role> findAllRole() {
		return roleRepo.findAll();
	}
	
	public List<Role> findChildRole(int roleId) {
		return roleRepo.findChildRoles(roleId);
	}

	public List<Role> findParentRole(int roleId) {
		return roleRepo.findParentRoles(roleId);
	}

	public List<String> findParentRoleNames(int roleId) {
		return roleRepo.findParentRoleNames(roleId);
	}

	/**
	 * Find only permission belongs directly to this role
	 * @param roleId
	 * @return
	 */
	public Set<Permission> findPermissionOfRole(int roleId) {
		return findPermissionOfRole(roleId, false);
	}
	
	/**
	 * Find all permission of role including permission from parent roles
	 * @param roleId
	 * @return
	 */
	public Set<Permission> findPermissionOfRole(int roleId, boolean includingParent) {
		Set<Permission> sp = new HashSet<>();
		if (!includingParent) {
			sp.addAll(permRepo.findByRoleId(roleId));
			return sp;
		}  else {
			Set<Role> sr = new HashSet<>();
			findRoleAndParentRole(sr, roleRepo.findOne(roleId));
			for (Role r: sr) {
				sp.addAll(permRepo.findByRoleId(r.getId()));
			}
			return sp;
		}
	}

	public List<Permission> getSortedPermissionList(int roleId, boolean includingParent) {
		Set<Permission> sp = findPermissionOfRole(roleId, includingParent);
		List<Permission> sortedList = new ArrayList<Permission>(sp);
		Collections.sort(sortedList, new Comparator<Permission>() {
			@Override
			public int compare(Permission o1, Permission o2) {
				int r = o1.getResource().compareTo(o2.getResource());
				if (r !=0) return r;
				r = o1.getAction().compareTo(o2.getAction());
				return r;
			}
		});
		return sortedList;
	}

	@Transactional
	public void updateParentRoles(Integer roleId, Integer[] parentRoleIds) {
		// remove existing parent roles
		rtRepo.removeParentRoles(roleId);
		rtRepo.flush();
		if (parentRoleIds == null) 
			return;
		// Re-add parent roles
		for (int i: parentRoleIds) {
			rtRepo.save(new RoleTree(roleId, i));
		}
	}

	public List<Role> findRoleOfGroup(int groupId) {
		return roleRepo.findRolesOfGroup(groupId);
	}
	
	public List<Role> findDirectRoleOfUser(int userId) {
		return roleRepo.findRolesOfUser(userId);
	}
	
	public List<String> findDirectRoleNameOfUser(int userId) {
		return roleRepo.findDirectRoleNameOfUser(userId);
	}

	public List<String> findRoleNameOfGroup(int groupId) {
		List<String> ls = new ArrayList<>();
		List<Role> lr = findRoleOfGroup(groupId);
		for (Role r: lr) {
			ls.add(r.getName());
		}
		return ls;
	}
	
	@Transactional
	public void updateRolesForGroup(Integer groupId, Integer[] roleIds) {
		
		log.debug("Setting roles " + Arrays.toString(roleIds) + " for group " + groupId);
		// remove existing roles
		grRepo.deleteByGroupId(groupId);
		grRepo.flush();

		if (roleIds == null) return;
		// Re-add parent roles
		for (int i: roleIds) {
			grRepo.save(new GroupRole(groupId, i));
		}
	}
	
	@Transactional
	public void updateRolesForUser(Integer userId, Integer[] roleIds) {

		log.debug("Setting roles " + Arrays.toString(roleIds) + " for user " + userId);
		// remove existing roles
		urRepo.deleteByUserId(userId);
		urRepo.flush();
		
		if (roleIds == null) return;
		// Re-add user roles
		for (int i: roleIds) {
			urRepo.save(new UserRole(userId, i));
		}
	}

	@Transactional
	public void addPermissionToRole(int roleId, int permissionId) {
		log.info("Add permission {} to role {}", permissionId, roleId);
		rpRepo.save(new RolePermission(roleId, permissionId));
	}
	
	/**
	 * Add permissions to role. Automatically insert new permission into DB if not exist,
	 * else assign existing permission to role
	 * @param id
	 * @param permList
	 */
	@Transactional
	public void addPermissionToRole(int id, List<Permission> permList) {
		log.info("Add permission {} to role " + id, permList);
		for (Permission p: permList) {
			if (p.getResource()==null || p.getAction()==null)
				continue;
			// Check if permission already exist in DB
			Permission pp = permRepo.findOne(p.getResource(), p.getAction(), p.getCondition(), p.getAllow());
			if (pp != null) {
				log.debug("Find existing permission: " + pp); 
				rpRepo.save(new RolePermission(id, pp.getId()));
			} else {
				p.setId(null);
				permRepo.save(p);
				log.info("Created new permission to assign to role: " + p);
				rpRepo.save(new RolePermission(id, p.getId()));
			}
		}
	}

	@Transactional
	public void deletePermssionFromRole(int roleId, int permissionId) {
		rpRepo.deleteByRoleIdAndPermissionId(roleId, permissionId);
		// Only delete the relation between roleId & permId. Still keep in permission table
		// for future re-use
	}

	@Transactional
	public void deleteAllPermssionFromRole(int roleId) {
		rpRepo.deleteByRoleId(roleId);
	}

	@Transactional
	public void deleteParentRole(int roleId, int parentId) {
		rtRepo.deleteByChildIdAndParentId(roleId, parentId);
	}

	public void addParentRole(int roleId, int parentId) {
		rtRepo.save(new RoleTree(parentId, roleId));
	}

	public Permission findPermission(int id) {
		return permRepo.findOne(id);
	}

	@Transactional
	public Permission createNewPermission(Permission newPermission) {
		return permRepo.save(newPermission);
	}

	public List<Role> findRoleUsePermission(int id) {
		return roleRepo.findRoleByPermissionId(id);
	}
}
