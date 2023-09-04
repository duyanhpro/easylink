package easylink.security;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import easylink.exception.AccessDeniedException;
import easylink.exception.UnauthenticatedException;

import javax.servlet.http.HttpSession;

public class SecurityUtil {

	static Logger log = LoggerFactory.getLogger(SecurityUtil.class);

	static Map<String, Expression> expressionCache = new HashMap<>();

	public static final String PRINCIPAL = "principal";
	
//	public static boolean STATELESS = true;	// default to stateless security (request-based).
//											// Or else Stateful (session-based) --> not fully tested yet !

	public static UserAuthorizationDetail getUserDetail() {
//		if (RequestContextHolder.getRequestAttributes() == null) return null;
//		if (STATELESS)
//			return  ((UserAuthorizationDetail)RequestContextHolder.getRequestAttributes().getAttribute(PRINCIPAL, RequestAttributes.SCOPE_REQUEST));
//		else {
//			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		    return (UserAuthorizationDetail)attr.getRequest().getSession(true).getAttribute(PRINCIPAL);
//		}
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession s = attr.getRequest().getSession(false);
		UserAuthorizationDetail uad;
		if (s != null) {
			log.trace("Session exists, Try to get UserDetail from session.");
			uad = (UserAuthorizationDetail) s.getAttribute(PRINCIPAL);        // web request. Session exists.
			if (uad != null) return uad;
		}

		log.trace("Get userDetail from request attributes");
		return (UserAuthorizationDetail) attr.getAttribute(PRINCIPAL, RequestAttributes.SCOPE_REQUEST);        // API request, no session

	}
	public static void setUserDetail(UserAuthorizationDetail userDetail) {
		RequestContextHolder.getRequestAttributes().setAttribute(PRINCIPAL, userDetail, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static boolean isAuthenticated() {
//		if (STATELESS)
//			return RequestContextHolder.getRequestAttributes().getAttribute(PRINCIPAL, RequestAttributes.SCOPE_REQUEST) != null;
//		else {
//			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		    return attr.getRequest().getSession(true).getAttribute(PRINCIPAL) != null;
//		}
		return getUserDetail() != null;
	}
	
	/**
	 * Check the condition using Spring Expression Language
	 * 
	 * @param resource
	 * @param action
	 * @param condition
	 * @param resourceInstance
	 * @param userDetail
	 * @return	true: condition match --> apply permission;  false: condition not match --> don't apply permission
	 */
	private static boolean checkCondition(String resource, String action, String condition,
			Object resourceInstance, UserAuthorizationDetail userDetail) {
		log.debug("Checking condition {} on resource instance {}", condition, resourceInstance);
		if (resourceInstance == null && condition.contains("#resource")) {
			log.debug("Null resourceInstance with a condition that includes #resource --> Skip check");
			return true;
		}
		Expression exp = expressionCache.get(condition);
		if (exp == null) {
			ExpressionParser parser = new SpelExpressionParser();
			exp = parser.parseExpression(condition);
			expressionCache.put(condition, exp);
		}

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("user",userDetail.getUser());
		if (resourceInstance!=null)
			context.setVariable("resource", resourceInstance);
		context.setVariable("action", action);
		context.setVariable("now", new Date()); // Future: Environment variable	// 
		boolean b = exp.getValue(context, Boolean.class);
		return b;
	}
	
	public static void authorize(String resource, String action) {
		authorize(resource, action, null);
	}

	public static List<String> findAllMatchingPermissions(UserAuthorizationDetail ud, String resource, String action) {
		// Find all resource:action in UserDetail that satisfy:  including resource:action, resource:*, *.*
		List<String> ps = new ArrayList<>();
		String per = "*:*";
		if (ud.getPermissionMap().containsKey(per))
			ps.add(per);

		if (!resource.equals("*")) {
			per = resource + ":*";
			if (ud.getPermissionMap().containsKey(per))
				ps.add(per);
		}

		if (!action.equals("*")) {
			per = resource + ":" + action;
			if (ud.getPermissionMap().containsKey(per))
				ps.add(per);
		}

		return ps;
	}

	/**
	 * Check if currently logged in user has permission to perform action on the object instance
	 * First check denied permission, then check allowed permission.
	 * User is granted when there is no denied permission and there is at least 1 allowed permission.
	 * 		1. Check permission on the resource:action)
	 * 		2. Check condition expression on the object instance
	 * This is accomplish by first get permission condition on the resource:action, then
	 * run the condition expression on the resource instance.
	 * Example: condition:  user.id==resource.createdUser
	 * 			resourceInstance = group RD
	 * --> will allow user when  user.getId() == (group RD).getCreatedUser()<br/>
	 * There is no need for @NeedPermission if using this method
	 * @param resource
	 * @param action
	 * @param resourceInstance:  object instance to check the condition
	 * @return:  Throw AccessDeniedException if no permission 
	 */
	public static void authorize(String resource, String action, Object resourceInstance) {
		UserAuthorizationDetail ud = getUserDetail();

		if (ud == null)
			throw new UnauthenticatedException("Người dùng chưa đăng nhập");        // no user --> not authenticated yet --> reject. NOT going to happend becaused checked in Authentication Interceptor

		// 2. Find the matching resource:action:condition in allow list and check condition (if any)
		// 2.1  find resource:action;  2.2 check condition;  Allow if no condition or any condition match
		if (!ud.getPermissionMap().isEmpty()) {
			//String per = findMatchingPermission(ud, resource, action);
			List<String> ps = findAllMatchingPermissions(ud, resource, action);		// check if user has any permission in resource:action, resouce:*, *.*
			for (String per : ps) {

				Set<String> conditions = ud.getConditions(per);
				//if (conditions == null) continue;
				for (String condition : conditions) {
					//if (condition == null || condition.isEmpty())    // allow without any condition
					if (condition.isEmpty())    // "" means no condition
						return;
					else        // allow  when any condition match (just need 1 condition match)
						if (checkCondition(resource, action, condition, resourceInstance, ud))
							return;
				}
			}
		}
		throw new AccessDeniedException("Không đủ quyền truy cập " + resource + ":" + action);
	}
	
	/**
	 * Return true if currently logged in user has permission to perform action on the resource
	 * @param resourceName
	 * @param action
	 * @return
	 */
	public static boolean hasPermission(String resourceName, String action) {
		return hasPermission(resourceName,action, 
			getUserDetail());
	}

	/**
	 * Use this method to show/hide display on GUI (called in UserAuthorizationDetail)
	 * Example:   th:if="${session.principal.hasPermission('dashboard:list')}"
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(String permission, UserAuthorizationDetail ud) {
		String[] a = permission.split(":");
		String resource = a[0];
		String action;
		if (a.length > 1)
			action = a[1];
		else
			return false;

		return hasPermission(resource, action, ud);
	}

	/**
	 * Return true if currently logged in user has permission to perform action on the resource
	 * This is to authorize Controller Method, so only check the resource:action, do not check condition (no object to check)
	 * @param resource
	 * @param action
	 * @return
	 */
	public static boolean hasPermission(String resource, String action, UserAuthorizationDetail ud) {
		if (ud == null) return false;

		// Check từ quyền lớn nhất trước.  Còn đối với deny thì check từ quyền nhỏ nhất trước
		String per = "*:*";
		if (!ud.getPermissionMap().containsKey(per)) {
			per = "*:" + action;
			if (!ud.getPermissionMap().containsKey(per)) {
				per = resource + ":*";
				if (!ud.getPermissionMap().containsKey(per)) {
					per = resource + ":" + action;
					if (!ud.getPermissionMap().containsKey(per)) {
						per = null;
					}
				}
			}
		}
		return per != null;
	}
	
	/**
	 * Return true if currently logged in user has one of the required roles
	 * @param requiredRoles
	 * @return
	 */
	public static boolean hasRole(String... requiredRoles) {
		return hasRole(requiredRoles, 
				getUserDetail());
	}
	
	/**
	 * Check if user has any of the required roles. Return true on first match
	 * @param requiredRoles
	 * @param user
	 * @return
	 */
	public static boolean hasRole(String[] requiredRoles, UserAuthorizationDetail user) {
		
		for (String r: requiredRoles) {
			if (user.getRoleCollection().contains(r))
				return true;
		}
		return false;
	}


	/**
	 * This method handles Authorization for @RequestHandler method
	 * 
	 * If user has any role in NeedRole annotation --> allow immediately
	 * If user does not have any role in NeedRole annotation or NeedRole not available, 
	 * check for NeedPermission annotation.
	 * User is granted access if he/she has this permission. 
	 * An AccessDeniedException is thrown if user doesn't satisfy both NeedRole and NeedPermission
	 * 
	 * return:  Throws AccessDeniedException
	 *
	 */
	public static void authorizeHandlerMethod(HandlerMethod hm) {
		
		log.debug("Authorize class {} method {}", hm.getBeanType().getSimpleName(), hm.getMethod().getName());
		UserAuthorizationDetail user = getUserDetail();
		if (user == null) {		// un-authenticated user --> throw immediately. If public URL then should exclude from this filter
			throw new UnauthenticatedException("Người dùng chưa đăng nhập");
			
			// Only allow if this is a public URL (no role, permission required).
			// If want to block unauthenticated --> configure Spring Security Config
//			if (hm.getBean().getClass().isAnnotationPresent(NeedRole.class)) {
//				throw new UnauthenticatedException("User not authenticated");
//			}
//			
//			// Check role on method
//			if (hm.hasMethodAnnotation(NeedRole.class))
//				throw new UnauthenticatedException("User not authenticated");
//			
//			// Check permission on method
//			if (hm.hasMethodAnnotation(NeedPermission.class))
//				throw new UnauthenticatedException("User not authenticated");
//
//			return;
		}

		boolean protectedMethod = false;
		// Check role on class
		if (hm.getBean().getClass().isAnnotationPresent(NeedRole.class)) {
			protectedMethod = true;
			String[] requiredRoles =  hm.getBean().getClass().getAnnotation(NeedRole.class).value();
			if (hasRole(requiredRoles, user))  {
				log.debug("User has the required roles of controller class");
				return;
			}
		}
		
		// Check role on method
		if (hm.hasMethodAnnotation(NeedRole.class)) {
			protectedMethod = true;
			String[] requiredRoles =  hm.getMethodAnnotation(NeedRole.class).value();
			if (hasRole(requiredRoles, user)) {
				log.debug("User has the required roles of controller method");
				return;
			}
		}

		// Check permission on method (Note:  NeedPermission can only have ONE action)
		if (hm.hasMethodAnnotation(NeedPermission.class)) {
			protectedMethod = true;
			String permission =  hm.getMethodAnnotation(NeedPermission.class).value();
			log.debug("Method required permission: " + permission);
			String[] a = permission.split(":");
			String resource = a[0];
			String action;
			if (a.length > 1)
				action = a[1];
			else 
				action = "*";
			
			if (hasPermission(resource, action, user)) {
				log.debug("User has the required permission of controller method");
				return;
			}
			else 
				throw new AccessDeniedException("Không có quyền truy cập tác vụ này");
		}
		
		if (protectedMethod)	// to catch any missing fish
			throw new AccessDeniedException("Không có quyền truy cập");
	}
	
	/**
	 * Add permission to the in-mem active user currently authenticated. Called when load permissions from DB
	 * @param resource
	 * @param action
	 * @param condition
	 */
	public static void addPermission(String resource, String action, String condition, boolean allow, UserAuthorizationDetail u) {
		
		if (action == null)
			action = "*";
		
		String actions[] = action.split(",");	// flatten permission resource:action1,action2 into resource:action1 & resource:action2

		for (String a: actions) {
			String permissionString = resource	+ ":" + a;
			if (allow) {
				if (!u.getPermissionMap().containsKey(permissionString)) { // new permission
					Set<String> c = new HashSet<>();
					if (condition == null || condition.equals("*"))
						condition = "";
					c.add(condition);
					u.getPermissionMap().put(permissionString, c);
				} else {	// already exists
					Set<String> c = u.getConditions(permissionString);
					if (!c.contains(""))	// already allow without any condition. Save memory: do not save other permissions
						c.add(condition);
				}
			} else {  // deny case.
				// REMOVED Until we find an use-case for deny permission
			}
		}
	}
	
	/**
	 * Add permission with condition = null & allow = true
	 * @param resource
	 * @param action
	 */
	public static void addPermission(String resource, String action) {
		addPermission(resource, action, null, true);
	}
	
	public static void addPermission(String resource, String action, String condition, boolean allow) {
		addPermission(resource, action, condition, allow, getUserDetail());
	}
	
	/**
	 * Temporary clear all permission in memory (do not persist). Permission will reset when user re-login
	 */
	public static void clearAllPermissionInSession() {
		UserAuthorizationDetail u = getUserDetail();
		u.getPermissionMap().clear();
	}

}
