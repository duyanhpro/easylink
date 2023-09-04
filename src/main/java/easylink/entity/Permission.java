package easylink.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_permission")
public class Permission extends BaseEntity {
	@Override
	public String toString() {
		return "Permission [" + (resource != null ? "resource=" + resource + ", " : "")
				+ (action != null ? "action=" + action + ", " : "")
				+ (condition != null ? "condition=" + condition + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (allow != null ? "allow=" + allow + ", " : "") + (id != null ? "id=" + id + ", " : "")
				+ (createdUser != null ? "createdUser=" + createdUser + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (modifiedUser != null ? "modifiedUser=" + modifiedUser + ", " : "")
				+ (modifiedDate != null ? "modifiedDate=" + modifiedDate : "") + "]";
	}
	String resource;
	String action;
	String condition;
	String description;
	Boolean allow = true;
	
	public Permission() {}
	public Permission(String resource, String action, String condition) {
		super();
		this.resource = resource;
		this.action = action;
		this.condition = condition;
	}
	public Permission(String resource, String action, String condition, String description, boolean allow) {
		super();
		this.resource = resource;
		this.action = action;
		this.condition = condition;
		this.description = description;
		this.allow = allow;
	}

	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Column(name = "action")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "condition")
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Boolean getAllow() {
		return allow;
	}
	public void setAllow(Boolean allow) {
		this.allow = allow;
	}
}
