package easylink.entity;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import easylink.dto.AlarmLevel;

/**
 */
public class Alarm {

	// Fields
	private String deviceToken;
	private String content;
	Date eventTime;
	String type;
	Date rawEventTime;
	
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

	public Alarm(String deviceToken, Date eventTime, String content, String type, AlarmLevel level, Integer ruleId, Date rawEventTime) {
		super();
		this.deviceToken = deviceToken;
		this.eventTime = eventTime;
		this.content = content;
		this.type = type;
		this.level = level;
		this.ruleId = ruleId;
		this.rawEventTime = rawEventTime;
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

	public Date getRawEventTime() {
		return rawEventTime;
	}

	public void setRawEventTime(Date rawEventTime) {
		this.rawEventTime = rawEventTime;
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