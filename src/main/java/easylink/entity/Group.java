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
public class Group extends BaseEntity implements Comparable<Group> {

	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	
	// Fields

	private String name;
	private Integer status = STATUS_ACTIVE;
	private Integer parentId = 0;
	
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
	public Group(String name, Integer parentId, Integer status, Integer createdUser) {
		this.name = name;
		this.status = status;
		this.createdUser = createdUser;
		this.parentId = parentId;
	}

	/** full constructor */
	public Group(String name, Integer status, Integer createdUser,Integer parentId,
			Date createdDate, Date modifiedDate, Integer modifiedUser) {
		this.name = name;
		this.status = status;
		this.parentId = parentId;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				", name='" + name +
				", status=" + status +
				", parentId=" + parentId +
				", roles=" + roles +
				", users=" + users +
				'}';
	}

	@Override
	public int compareTo(Group o) {
		return Integer.compare(this.id, o.id);
	}
}