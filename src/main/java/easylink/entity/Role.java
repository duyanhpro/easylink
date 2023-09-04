package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_role")
public class Role extends BaseEntity {
	String name;
	String description;
	Integer status = STATUS_INACTIVE;
	Boolean editable = true;
	
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (description != null)
			builder.append("description=").append(description).append(", ");
		if (status != null)
			builder.append("status=").append(status).append(", ");
		if (editable != null)
			builder.append("editable=").append(editable);
		builder.append("]");
		return builder.toString();
	}
	public Role() {}
	public Role(String name) {
		super();
		this.name = name;
	}
	public Role(String name, String description, Integer status) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
	}
	public Role(String name, String description, Integer status, Boolean editable) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
		this.editable = editable;
	}
}
