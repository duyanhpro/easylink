package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_rule_device")
public class RuleDevice extends BaseEntity {
	Integer ruleId;
	Integer deviceId;
	String deviceToken;
	
	public RuleDevice(Integer ruleId, Integer deviceId, String deviceToken) {
		super();
		this.ruleId = ruleId;
		this.deviceId = deviceId;
		this.deviceToken = deviceToken;
	}

	public RuleDevice() {

	}

	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
	@Override
	public String toString() {
		return "RuleDevice [" + (ruleId != null ? "ruleId=" + ruleId + ", " : "")
				+ (deviceId != null ? "deviceId=" + deviceId + ", " : "")
				+ (deviceToken != null ? "deviceToken=" + deviceToken : "") + "]";
	}
	
	
}
