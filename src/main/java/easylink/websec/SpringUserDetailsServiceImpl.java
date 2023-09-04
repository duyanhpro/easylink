package easylink.websec;

import java.util.ArrayList;
import java.util.List;

import easylink.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import easylink.repository.UserRepository;
import easylink.service.RolePermissionService;

/**
 * Custom implementation of Spring Security's UserDetailsService 
 * This will be used by DaoAuthenticationProvider in WebSecurityConfig to load the UserDetails object for SpringSecurity
 * @author Pham Duy Anh
 *
 */
@Service
public class SpringUserDetailsServiceImpl implements UserDetailsService {

	static Logger log = LoggerFactory.getLogger(SpringUserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
    RolePermissionService rpService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// Load user
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " not found");
		}

		// Give SpringSecurity a basic role (we will not use this)
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}

}
