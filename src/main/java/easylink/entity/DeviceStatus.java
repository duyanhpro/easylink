package easylink.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * User entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_device_status")
public class DeviceStatus {

	// runtime status
	public static final int STATUS_OK = 1;
	public static final int STATUS_NOK = 0;	// lost connection
	
	@Id
	String deviceToken;
	
	@JsonIgnore
	String telemetry;
	
	Date eventTime;
	private Integer status = STATUS_NOK;

	@Transient
	@JsonInclude
	Map<String, Object> telemetryMap = new HashMap<>();
	
	// Constructors
	/** default constructor */
	public DeviceStatus() {
	}

	public DeviceStatus(String deviceToken2) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getTelemetry() {
		return telemetry;
	}

	public void setTelemetry(String telemetry) {
		this.telemetry = telemetry;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DeviceStatus [" + (deviceToken != null ? "deviceToken=" + deviceToken + ", " : "")
				+ (telemetry != null ? "telemetry=" + telemetry + ", " : "")
				+ (eventTime != null ? "eventTime=" + eventTime + ", " : "")
				+ (status != null ? "status=" + status : "") + "]";
	}

	public Map<String, Object> getTelemetryMap() {
		return telemetryMap;
	}

	public void setTelemetryMap(Map<String, Object> telemetryMap) {
		this.telemetryMap = telemetryMap;
	}

}