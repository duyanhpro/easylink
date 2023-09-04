package easylink.websec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Handler when login failed
 * @author Pham Duy Anh
 *
 */
public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	static Logger log = LoggerFactory.getLogger(WebAuthenticationFailureHandler.class);
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
			AuthenticationException exception) throws IOException, ServletException {
		
		log.error("Authentication failure: " + exception.getClass().getName() + " - " + exception.getMessage());
		
		String errorMsg = "Login error";
		
		if(exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			errorMsg = "Invalid password";
		} else if(exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
			errorMsg = "Username not found";
		} else if (exception.getClass().isAssignableFrom(LockedException.class)) {
			errorMsg = "User has been locked";
		} else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
			errorMsg = "User has been disabled";
		} else {
			errorMsg = exception.getMessage();		// not secured. should not do in production!!!
		}
		
		response.sendRedirect("login?errorMsg=" + errorMsg);
		
	}
}
