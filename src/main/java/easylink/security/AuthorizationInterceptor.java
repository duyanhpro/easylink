package easylink.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * This interceptor handles Authorization for all controller methods (both Web & REST API)
 * 
 * If user has any role in NeedRole annotation --> allow immediately
 * If user does not have any role in NeedRole annotation or NeedRole not available, 
 * check for NeedPermission annotation.
 * User is granted access if he/she has this permission. 
 * An AccessDeniedException is thrown if user doesn't satisfy both NeedRole and NeedPermission
 * 
 * @author Pham Duy Anh
 *
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

	static Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("Handling authorization for request {}", request.getRequestURI());
		HandlerMethod hm;
		if (!(handler instanceof ResourceHttpRequestHandler))
			hm =(HandlerMethod)handler;
		else {
			return true; 
		}

		if (!SecurityUtil.isAuthenticated()) {
			log.debug("User not authenticated! This can't happen in production, only in devMode due to liveReload");
			response.sendRedirect(request.getContextPath() + "/login");  // note:  must include context-path
			return false;
		}

		// Authorize user and throw AccessDeniedException if not authorized
		SecurityUtil.authorizeHandlerMethod(hm);

		return true;	// always return true to go to next in filter chain 	
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
