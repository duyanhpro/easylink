package easylink.websec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import easylink.common.Constant;
import easylink.service.UserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import easylink.security.SecurityUtil;
import easylink.security.UserAuthorizationDetail;
import easylink.service.UserAuthorizationService;

public class WebAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	static Logger log = LoggerFactory.getLogger(WebAuthenticationSuccessHandler.class);
	
	@Autowired
    UserAuthorizationService userService;
	@Autowired
	UserGroupService ugService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// Get username from Spring Security login
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// Load userDetail with all roles & permission for our own authorization framework
		UserAuthorizationDetail uad = userService.loadUserByUsername(username);
		log.debug("Authentication success. Logged-in user: {}", uad);

		// Add userDetail to session
		HttpSession session = request.getSession();
		session.setAttribute(SecurityUtil.PRINCIPAL, uad);

		// Set session attribute to use on thymeleaf HTML  (except "principal" is set inside SecurityUtil.onWebAu...Success
		session.setAttribute(Constant.SESSION_USER, uad.getUser());

		// Customize:  set value of session when user is in root group:
		session.setAttribute("isAdmin", uad.getRoleCollection().contains("ADMIN"));
		session.setAttribute("isRoot", ugService.isInRootGroup(uad.getUserId()));

		//set our response to OK status
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("");

		// We can choose to redirect to different location depends on user information
//		if (uad.getUser().getType() == User.TYPE_ADMIN)
//			response.sendRedirect("home");
//		else
//			response.sendRedirect("dashboard");

	}

}
