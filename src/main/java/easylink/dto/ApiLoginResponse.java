package easylink.dto;

import easylink.entity.User;

public class ApiLoginResponse {
	User user;
	String token;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ApiLoginResponse(User user, String token) {
		super();
		this.user = user;
		this.token = token;
	}
}
