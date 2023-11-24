package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * User entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_device")
public class Device extends BaseEntity {

	// Fields
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;

	private String name;
	String description;
	String location;
	String street;
	String district;
	String city;
	String deviceToken;
	String tags;

	Float lon;
	Float lat;
	
	private Integer status = STATUS_ACTIVE;
	Integer schemaId = 1;
	Integer groupId = 0;
	Integer typeId = 1;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
// Constructors

	/** default constructor */
	public Device() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String group) {
		this.tags = group;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Integer getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(Integer deviceTypeId) {
		this.schemaId = deviceTypeId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return "Device{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", location='" + location + '\'' +
				", street='" + street + '\'' +
				", district='" + district + '\'' +
				", city='" + city + '\'' +
				", deviceToken='" + deviceToken + '\'' +
				", tags='" + tags + '\'' +
				", lon=" + lon +
				", lat=" + lat +
				", status=" + status +
				", schemaId=" + schemaId +
				", groupId=" + groupId +
				", typeId=" + typeId +
				'}';
	}

}