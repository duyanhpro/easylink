package easylink.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import easylink.dto.AlarmLevel;

/**
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_alarm")
public class Alarm extends BaseEntity {

	// Fields
	private String deviceToken;
	private String content;
	Date eventTime;
	String type;
	
	@Enumerated(EnumType.ORDINAL)
    AlarmLevel level;
	
	Integer ruleId;
	
	// Constructors

	/** default constructor */
	public Alarm() {
	}

	public Alarm(String deviceToken, Date eventTime, String content, String type, AlarmLevel level, Integer ruleId) {
		super();
		this.deviceToken = deviceToken;
		this.eventTime = eventTime;
		this.content = content;
		this.type = type;
		this.level = level;
		this.ruleId = ruleId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AlarmLevel getLevel() {
		return level;
	}

	public void setLevel(AlarmLevel level) {
		this.level = level;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}


	@Override
	public String toString() {
		return "Alarm [" 
				+ (deviceToken != null ? "deviceToken=" + deviceToken + ", " : "")
				+ (content != null ? "content=" + content + ", " : "")
				+ (eventTime != null ? "eventTime=" + eventTime + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + (level != null ? "level=" + level + ", " : "")
				+ (ruleId != null ? "ruleId=" + ruleId : "") + "]";
	}

}