package easylink.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import easylink.entity.User;
import easylink.exception.AccessDeniedException;
import easylink.exception.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import easylink.service.ConfigParamService;
import easylink.service.GroupService;
import easylink.service.LocaleService;
import easylink.service.RolePermissionService;
import easylink.service.UserGroupService;
import easylink.service.UserService;

public class BaseController {
	
	Logger log = LoggerFactory.getLogger(this.getClass());	// OK because spring controller is singleton
	
	@Autowired
	HttpSession session;
	
	@Autowired
    UserGroupService ugService;
	
	@Autowired
    UserService userService;
	
	@Autowired
    GroupService groupService;
	
	@Autowired
    RolePermissionService rpService;
	
	@Autowired
    LocaleService localeService;
	
	@Autowired
	ConfigParamService configService;
	
	protected User getLoginUser() {
		return (User)session.getAttribute("user");
	}

	@ExceptionHandler(UnauthenticatedException.class)
	public ModelAndView handleUnAuthenticatedUser(HttpServletRequest req, Exception ex) {
		log.debug("UnauthenticatedException --> redirect to login page");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("layout/login");
		return mav;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleAccesDenied(HttpServletRequest req, Exception ex) {
		log.debug("User doesn't have permission. Show error ", ex);
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageError", ex.getClass().getSimpleName());
		mav.setViewName("layout/error");		// full path to html template because exception is not affected by ThymeleafInterceptor
		return mav;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		log.error("Request: " + req.getRequestURL() + " raised exception " + ex.getClass().getSimpleName() +": " + ex.getMessage());
		log.debug("Exception: ",ex);

		ModelAndView mav = new ModelAndView();

		// TODO:  different message for each type of exception
		mav.addObject("pageError", ex.getClass().getSimpleName());

		mav.setViewName("layout/error");		// full path to html template because exception is not affected by ThymeleafInterceptor
		return mav;
	}

}

