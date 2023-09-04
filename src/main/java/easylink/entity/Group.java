package easylink.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Group entity. @author MyEclipse Persistence Tools
 */

        
@Entity
@Table(name = "tbl_group")
public class Group extends BaseEntity {

	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	
	// Fields

	private String name;
	private Integer status = STATUS_INACTIVE;
	
	List<Role> roles;
	List<User> users;
	
	@Transient
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@Transient
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	/** default constructor */
	public Group() {
	}

	/** minimal constructor */
	public Group(String name, Integer status, Integer createdUser) {
		this.name = name;
		this.status = status;
		this.createdUser = createdUser;
	}

	/** full constructor */
	public Group(String name, Integer status, Integer createdUser,
			Date createdDate, Date modifiedDate, Integer modifiedUser) {
		this.name = name;
		this.status = status;
		this.createdUser = createdUser;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.modifiedUser = modifiedUser;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (status != null)
			builder.append("status=").append(status).append(", ");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (createdUser != null)
			builder.append("createdUser=").append(createdUser).append(", ");
		if (createdDate != null)
			builder.append("createdDate=").append(createdDate).append(", ");
		if (modifiedUser != null)
			builder.append("modifiedUser=").append(modifiedUser).append(", ");
		if (modifiedDate != null)
			builder.append("modifiedDate=").append(modifiedDate);
		builder.append("]");
		return builder.toString();
	}
}