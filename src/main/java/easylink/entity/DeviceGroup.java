package easylink.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_device_group")
@IdClass(DeviceGroup.class)
public class DeviceGroup implements Serializable {

	@Id
	private Integer deviceId;
	@Id
	private Integer groupId;
	@Id
	Boolean inherit = false;

	/** default constructor */
	public DeviceGroup() {
	}

	public DeviceGroup(Integer deviceId, Integer groupId, Boolean inherit) {
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.inherit = inherit;
	}

	public DeviceGroup(int deviceId, int groupId) {
		this.deviceId = deviceId;
		this.groupId = groupId;
	}

	@Column(name = "device_id", nullable = false)
	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "group_id", nullable = false)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Boolean getInherit() {
		return inherit;
	}

	public void setInherit(Boolean inherit) {
		this.inherit = inherit;
	}
}