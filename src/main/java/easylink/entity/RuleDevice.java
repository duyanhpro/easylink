package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_rule_device")
public class RuleDevice extends BaseEntity {
	Integer ruleId;
	Integer deviceId;
	
	public RuleDevice(Integer ruleId, Integer deviceId) {
		super();
		this.ruleId = ruleId;
		this.deviceId = deviceId;
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

	@Override
	public String toString() {
		return "RuleDevice [" + (ruleId != null ? "ruleId=" + ruleId + ", " : "")
				+ (deviceId != null ? "deviceId=" + deviceId + ", " : "")
				+ "]";
	}
	
	
}
