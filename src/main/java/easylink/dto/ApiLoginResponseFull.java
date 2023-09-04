package easylink.dto;

import easylink.security.UserAuthorizationDetail;

public class ApiLoginResponseFull {
	UserAuthorizationDetail userDetail;
	String token;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ApiLoginResponseFull(UserAuthorizationDetail userDetail, String token) {
		super();
		this.userDetail = userDetail;
		this.token = token;
	}
	public UserAuthorizationDetail getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserAuthorizationDetail userDetail) {
		this.userDetail = userDetail;
	}
	
}
