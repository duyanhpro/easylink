package easylink.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_device_group_full")
public class DeviceGroupFull {

	private Integer deviceId;
	private Integer groupId;
	Boolean inherit = false;

	/** default constructor */
	public DeviceGroupFull() {
	}

	public DeviceGroupFull(Integer deviceId, Integer groupId, Boolean inherit) {
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.inherit = inherit;
	}

	public DeviceGroupFull(int deviceId, int groupId) {
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