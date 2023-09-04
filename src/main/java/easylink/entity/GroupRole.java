package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_group_role")
public class GroupRole extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer groupId;
	private Integer roleId;

	/** default constructor */
	public GroupRole() {
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public GroupRole(Integer groupId, Integer roleId) {
		super();
		this.groupId = groupId;
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "GroupRole [groupId=" + groupId + ", roleId=" + roleId + "]";
	}



}