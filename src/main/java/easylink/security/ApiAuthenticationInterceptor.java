package easylink.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import easylink.common.Constant;
import easylink.service.TokenService;
import easylink.service.UserAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * API thực hiện xác thực bằng token. Kiểm tra nếu có token hợp lệ thì cho tiếp tục và lưu token vào RequestContextHolder. 
 * Nếu token không tồn tại thì throw UnauthenticatedException
 * Token lấy từ header:  Constant.AUTHENTICATION_HEADER = "X-Auth-Token"
 * @author phamd
 *
 */
@Component
public class ApiAuthenticationInterceptor implements HandlerInterceptor {

	static Logger log = LoggerFactory.getLogger(ApiAuthenticationInterceptor.class);
	
	@Autowired
    TokenService tokenService;
	
	@Autowired
    UserAuthorizationService uaService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Authen by session (this API request is from within a web session)
		HttpSession session = request.getSession();
		UserAuthorizationDetail userDetail = null;
		userDetail = (UserAuthorizationDetail)session.getAttribute(SecurityUtil.PRINCIPAL);
		if (userDetail != null) {
			log.debug("API Authentication: --> Find user from session: {}", userDetail);
		} else {

			// Authen by token
			String token = request.getHeader(Constant.AUTHENTICATION_HEADER);
			log.debug("Token: " + token);

			if (token == null) {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				return false;
			}

			userDetail = (UserAuthorizationDetail) tokenService.getUserDetailFromToken(token);
			log.debug("API Authentication header: {} --> Find user from token: {}", token, userDetail);
		}


		// Verify authentication
		if (userDetail!=null) {
			Thread.currentThread().setName(userDetail.getUser().getUsername() + "-" + Thread.currentThread().getId());	// set thread name for logging
			
			SecurityUtil.setUserDetail(userDetail);		// add userDetail into requestContext to handle authorisation & auditing
			
			return true;
		} else {
			
			// DEBUG: assign a default user for quicker testing
//			userDetail = uaService.loadUserByUsername("anhpd");
//			RequestContextHolder.getRequestAttributes().setAttribute("user", userDetail, RequestAttributes.SCOPE_REQUEST);
//			if (true) return true;
			
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
