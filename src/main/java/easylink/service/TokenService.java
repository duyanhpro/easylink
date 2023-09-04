package easylink.service;

import org.springframework.stereotype.Service;

import easylink.security.UserAuthorizationDetail;

@Service
public interface TokenService {
	/**
	 * Generate random token & save in local map or redis
	 * @param userDetail
	 * @return
	 */
	String generateToken(UserAuthorizationDetail userDetail);
	
	/**
	 * Reset userDetail for this token
	 * @param userDetail
	 * @return
	 */
	void updateToken(String token, UserAuthorizationDetail userDetail);
	
	/**
	 * Load user with permission & roles from token
	 * @param token
	 * @return
	 */
	UserAuthorizationDetail getUserDetailFromToken(String token);
	
	boolean isTokenExist(String token);
	
	/**
	 * Remove token from database
	 * @param token
	 */
	void clearToken(String token);
	
}
