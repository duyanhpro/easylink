package easylink.service;

import java.util.List;
import java.util.Set;

import easylink.entity.Group;
import easylink.entity.Permission;
import easylink.entity.Role;
import easylink.entity.User;
import easylink.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easylink.exception.AccessDeniedException;
import easylink.repository.UserRepository;
import easylink.security.SecurityUtil;
import easylink.security.UserAuthorizationDetail;

/**
 * Special service to load all authorization information of users (all roles + permissions) 
 * @author Pham Duy Anh
 *
 */
@Service
public class UserAuthorizationService {

	static Logger log = LoggerFactory.getLogger(UserAuthorizationService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepo;
	
	@Autowired
	RolePermissionService rpService;

	public UserAuthorizationDetail loadUserByUsername(String username) throws AccessDeniedException {

		// Load user
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new AccessDeniedException("Username " + username + " not found");
		}
		if (user.getStatus() == User.STATUS_INACTIVE)
			throw new AccessDeniedException("User is disabled");

		UserAuthorizationDetail ud = new UserAuthorizationDetail();		// Collection of user's authorities (role, permissions)

		ud.setUserId(user.getId());
		ud.setUsername(username);
		ud.setUser(user);
		
		// Load all roles 
		Set<Role> roleSet = rpService.findAllRoleOfUser(user.getId());
		for (Role r: roleSet) {
			ud.getRoleCollection().add(r.getName());
		}

		// Load all permission from all roles
		Set<Permission> perms = rpService.findAllPermissionOfUser(user.getId());
		
		// Build some data structure to help permission checking
		for (Permission p: perms) {
			SecurityUtil.addPermission(p.getResource(), p.getAction(), p.getCondition(), p.getAllow(), ud);
		}
		
		log.debug("Loaded User Detail: " + ud);
		
		return ud;
	}

}
