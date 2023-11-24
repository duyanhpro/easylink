package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_dashboard")
public class Dashboard extends BaseEntity {
	String name;
	String description;
	String url;
	String metadata;
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;
	private Integer status = STATUS_ACTIVE;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
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

	@Override
	public String toString() {
		return "Dashboard{" +
				"id=" + id + '\'' +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				", metadata='" + metadata + '\'' +
				", status=" + status +
				'}';
	}
}
