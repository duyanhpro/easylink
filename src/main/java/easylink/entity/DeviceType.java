package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Device Type entity. Include schema information.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_device_type")
public class DeviceType extends BaseEntity {


	private String name;
	String description;

	public DeviceType() {
	}

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

	@Override
	public String toString() {
		return "DeviceType{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}