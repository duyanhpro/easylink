package easylink.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserWithGroupRole {
	int id;
	String username;
	String email;
	int status;
	List<String> groupNames = new ArrayList<>();
	Set<String> roleNames = new HashSet<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDto [id=").append(id).append(", ");
		if (username != null)
			builder.append("username=").append(username).append(", ");
		if (email != null)
			builder.append("email=").append(email).append(", ");
		builder.append("status=").append(status).append(", ");
		if (groupNames != null)
			builder.append("groupNames=").append(groupNames).append(", ");
		if (roleNames != null)
			builder.append("roleNames=").append(roleNames);
		builder.append("]");
		return builder.toString();
	}
	public List<String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}
	public Set<String> getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(Set<String> roleNames) {
		this.roleNames = roleNames;
	}
	
	
}
