package easylink.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Device Type entity. Include schema information.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_schema")
public class DeviceSchema extends BaseEntity {


	private String name;
	String description;
	String dataSchema;
	String topic;
	String tableName;

	public DeviceSchema() {
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

	@Override
	public String toString() {
		return "DeviceSchema{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", dataSchema='" + dataSchema + '\'' +
				", topic='" + topic + '\'' +
				", tableName='" + tableName + '\'' +
				'}';
	}
}