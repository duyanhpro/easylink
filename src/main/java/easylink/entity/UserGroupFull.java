package easylink.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_user_group_full")
public class UserGroupFull {

	private Integer userId;
	private Integer groupId;
	Boolean inherit = false;

	/** default constructor */
	public UserGroupFull() {
	}

	public UserGroupFull(Integer userId, Integer groupId, Boolean inherit) {
		this.userId = userId;
		this.groupId = groupId;
		this.inherit = inherit;
	}

	public UserGroupFull(int userId, int groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "group_id", nullable = false)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Boolean getInherit() {
		return inherit;
	}

	public void setInherit(Boolean inherit) {
		this.inherit = inherit;
	}
}