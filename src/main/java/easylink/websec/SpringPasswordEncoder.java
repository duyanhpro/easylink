package easylink.websec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import easylink.security.CustomPasswordEncoder;

public class SpringPasswordEncoder implements PasswordEncoder {

	@Autowired
    CustomPasswordEncoder passwordEncoder;
	
    @Override
    public String encode(CharSequence rawPassword) {
 
        return passwordEncoder.encode(rawPassword);
    }
 
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	return passwordEncoder.matches(rawPassword, encodedPassword);
    }
 
}