package easylink;

import java.util.*;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import easylink.entity.User;
import easylink.security.UserAuthorizationDetail;

/**
 * This class is used to inject userDetail for the SecurityPolicyTest
 * @author phamd
 *
 */

public class TestUserDetailsService  {

	public UserAuthorizationDetail loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserAuthorizationDetail user = new UserAuthorizationDetail(); //"testUser", "asdf##$@#", true, true, true, true, new HashSet<>());
		user.setUser(new User("testUser", "123456", "Test User", 1, "anhpd", "09123456789", null, null, 1));
		user.getUser().setId(1);
		
		Set<String> perms = new HashSet<>(Arrays.asList("user:create", "user:edit", "user:view", "user:list", 
				"group:edit:user.id=resource.createdUser"));
		Set<String> roles = new HashSet<>(Arrays.asList("admin", "guest", "dealer"));
		Map<String, Set<String>> permCon = new HashMap<>();
		for (String p: perms) {
			permCon.put(p, null);
		}
		user.setRoleCollection(roles);
		user.setPermissionMap(permCon);
		
		return user;
	}

}
