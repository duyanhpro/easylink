package easylink.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * User entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {

	// Fields

	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	
	public static final int TYPE_SYSTEM = 0;	// internal user
	public static final int TYPE_CUSTOMER = 1;	// Customer
	
	private String username;
	@JsonIgnore
	private String password;

	private String fullName;
	private String email;
	private String phone;
	
	private Integer status = STATUS_ACTIVE;
	private Integer type = TYPE_SYSTEM;		// Reserved. Not used in this project.
	
	// Constructors

	/** default constructor */
	public User() {
	}

	@Column(name = "username", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "full_name")
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone", length = 20)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "password", nullable = false, length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public User(String username, String password, String fullName, Integer type, String email, String phone,
			String title, String company, Integer status) {
		super();
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.type = type;
		this.email = email;
		this.phone = phone;
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [");
		if (username != null)
			builder.append("username=").append(username).append(", ");
		if (fullName != null)
			builder.append("fullName=").append(fullName).append(", ");
		if (email != null)
			builder.append("email=").append(email).append(", ");
		if (phone != null)
			builder.append("phone=").append(phone).append(", ");
		if (password != null)
			builder.append("password=").append(password).append(", ");
		if (status != null)
			builder.append("status=").append(status).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
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