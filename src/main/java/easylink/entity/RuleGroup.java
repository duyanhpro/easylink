package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_rule_group")
public class RuleGroup extends BaseEntity {
	Integer ruleId;
	Integer groupId;

	public RuleGroup(Integer ruleId, Integer groupId) {
		super();
		this.ruleId = ruleId;
		this.groupId = groupId;
	}

	public RuleGroup() {

	}

	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "RuleDevice [" + (ruleId != null ? "ruleId=" + ruleId + ", " : "")
				+ (groupId != null ? "deviceId=" + groupId + ", " : "") + "]";
	}
	
	
}
