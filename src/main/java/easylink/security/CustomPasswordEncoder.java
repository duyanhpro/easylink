package easylink.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Encode password using Bcrypt when saving into DB and when Spring Security authenticate
 * Change to other encryption format depending on requirement
 * @author phamd
 *
 */
@Service
public class CustomPasswordEncoder  {
	 
    public String encode(CharSequence rawPassword) {
 
    	String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
    	// gensalt's log_rounds parameter determines the complexity
    	// the work factor is 2**log_rounds, and the default is 10
        return hashed;
    }
 
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	try {
    		return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    	} catch (Exception e) {
    		return false;			
    	}
    }
 
}