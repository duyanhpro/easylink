package easylink.controller.api;

import javax.servlet.http.HttpServletRequest;

import easylink.common.Constant;
import easylink.dto.ApiLoginRequest;
import easylink.dto.ApiLoginResponse;
import easylink.security.SecurityUtil;
import easylink.security.UserAuthorizationDetail;
import easylink.service.TokenService;
import easylink.service.UserAuthorizationService;
import easylink.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api")
@Tag(name="authentications", description="Authentication API (Login & Logout)")
public class AuthenticationController {

	static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private UserAuthorizationService userDetailsService;
	
	@Autowired
	private TokenService tokenService;
//	
	@Autowired
	private UserService userService;

	@PostMapping(value = "/login")
	@Operation(summary="Login to get the authentication token")
	public ResponseEntity<ApiLoginResponse> login(@RequestBody ApiLoginRequest loginRequest) {
		try {
			
			log.debug("Login request: " + loginRequest.getUsername());
			
			userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

			UserAuthorizationDetail userDetail = (UserAuthorizationDetail) userDetailsService.loadUserByUsername(loginRequest.getUsername());
			
			String token = tokenService.generateToken(userDetail);
			
			return new ResponseEntity<>(new ApiLoginResponse(userDetail.getUser(), token), HttpStatus.OK);
			
		} catch (Exception e) {		
			log.debug("", e);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(value = "/logout")
	@Operation(summary="Log out. Authentication token will be removed.")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		
		String token = request.getHeader(Constant.AUTHENTICATION_HEADER);
		tokenService.clearToken(token);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/refreshToken")
	@Operation(summary="Reload user detail information for token")
	public UserAuthorizationDetail reload(HttpServletRequest request) {
		
		UserAuthorizationDetail userDetail = (UserAuthorizationDetail) userDetailsService.loadUserByUsername(SecurityUtil.getUserDetail().getUser().getUsername());
		SecurityUtil.setUserDetail(userDetail);
		String token = request.getHeader(Constant.AUTHENTICATION_HEADER);
		tokenService.updateToken(token, userDetail);
		return userDetail;
	}
	
}
