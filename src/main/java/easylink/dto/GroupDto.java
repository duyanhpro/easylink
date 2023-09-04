package easylink.dto;

/**
 * Sample DTO class to demo spring data jpa for group repository
 */
public class GroupDto  {
	
	Integer id;
	private String name;
	private Integer status;
	private String roleName;		// First role of group. Just for demo
	
	// Constructors

	/** default constructor */
	public GroupDto() {
	}

	public GroupDto(Integer id, String name, Integer status, String roleName) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.roleName = roleName;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupDto [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (status != null)
			builder.append("status=").append(status).append(", ");
		if (roleName != null)
			builder.append("roleName=").append(roleName);
		builder.append("]");
		return builder.toString();
	}

	
}