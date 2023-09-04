package easylink.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import easylink.entity.User;

@SuppressWarnings("serial")
/**
 * Represent authenticated user with all permissions and roles. Will be loaded from DB after user log in
 * @author Pham Duy Anh
 *
 */
public class UserAuthorizationDetail {

	private int userId;		// custom field to audit entity create/update
	private String username;
	private User user;
	private Set<String> roleCollection = new HashSet<>();
	private Map<String, Set<String>> permCon = new HashMap<>(); //  Map(resource:action, Set<condition>)

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<String> getRoleCollection() {
		return roleCollection;
	}

	public void setRoleCollection(Set<String> roleCollection) {
		this.roleCollection = roleCollection;
	}

	public Map<String, Set<String>> getPermissionMap() {
		return permCon;
	}

	public void setPermissionMap(Map<String, Set<String>> permissionMap) {
		this.permCon = permissionMap;
	}

	/**
	 * Return set of conditions according to resource:action
	 * @param permission
	 * @return
	 */
	public Set<String> getConditions(String permission) {
		return permCon.get(permission);
	}

//	public void setConditionMap(Map<String, String> conditionMap) {
//		this.conditionMap = conditionMap;
//	}

	@Override
	public String toString() {
		return "UserAuthorizationDetail{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", user=" + user +
				", roleCollection=" + roleCollection +
				", permissions=" + permCon +
				'}';
	}

	
	/**
	 * Use this method to show/hide display on GUI
	 * Example:   th:if="${session.principal.hasPermission('dashboard:list')}"
	 * @param permission
	 * @return
	 */
	public boolean hasPermission(String permission) {
		return (SecurityUtil.hasPermission(permission, this));
	}
	
	public boolean hasRole(String role) {
		return roleCollection.contains(role);
	}
}
