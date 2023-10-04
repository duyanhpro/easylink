package easylink.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_device_type")
public class DeviceType extends BaseEntity {

	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;

	private String name;
	String description;
	String dataSchema;
	String topic;
	String tableName;

	private Integer status = STATUS_INACTIVE;

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

	public String getDataSchema() {
		return dataSchema;
	}

	public void setDataSchema(String dataSchema) {
		this.dataSchema = dataSchema;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DeviceType{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", dataSchema='" + dataSchema + '\'' +
				", topic='" + topic + '\'' +
				", tableName='" + tableName + '\'' +
				", status=" + status +
				'}';
	}
}