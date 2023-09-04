package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_role_permission")
public class RolePermission extends BaseEntity {
	int roleId;
	int permissionId;
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	public RolePermission(int roleId, int permissionId) {
		super();
		this.roleId = roleId;
		this.permissionId = permissionId;
	}
	public RolePermission() {
		super();
	}
}
