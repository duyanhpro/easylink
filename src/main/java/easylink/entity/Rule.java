package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_rule")
public class Rule extends BaseEntity {
	String name;
	String condition;
	String action;
	String actionType;
	
	Integer status = STATUS_INACTIVE;
	Integer scope = 0;
	Integer minInterval = 60;	// min interval between 2 action trigger, in seconds
	
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	public static final int SCOPE_ALL_DEVICES = 1;
	public static final int SCOPE_PER_DEVICE = 0;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	}
	
	public Integer getMinInterval() {
		return minInterval;
	}
	public void setMinInterval(Integer minInterval) {
		this.minInterval = minInterval;
	}
	
	@Override
	public String toString() {
		return "Rule [" + (name != null ? "name=" + name + ", " : "")
				+ (condition != null ? "condition=" + condition + ", " : "")
				+ (action != null ? "action=" + action + ", " : "")
				+ (actionType != null ? "actionType=" + actionType + ", " : "")
				+ (status != null ? "status=" + status + ", " : "") + (scope != null ? "scope=" + scope + ", " : "")
				+ (minInterval != null ? "minInterval=" + minInterval : "") + "]";
	}
	public Rule() {
		super();
	}
	public Rule(Integer id, String name, String condition, String action, String actionType, Integer status, Integer interval) {
		super();
		this.id = id;
		this.name = name;
		this.condition = condition;
		this.action = action;
		this.actionType = actionType;
		this.status = status;
		this.minInterval = interval;
	}
	
}
