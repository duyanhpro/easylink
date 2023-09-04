package easylink.dto;

public class ActionCreateAlarm {
	String alarmType;
	String alarmContent;
	String alarmLevel;
	@Override
	public String toString() {
		return "ActionCreateAlarm [" + (alarmType != null ? "alarmType=" + alarmType + ", " : "")
				+ (alarmContent != null ? "alarmContent=" + alarmContent + ", " : "")
				+ (alarmLevel != null ? "alarmLevel=" + alarmLevel : "") + "]";
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmContent() {
		return alarmContent;
	}
	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}
	public String getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	
	public ActionCreateAlarm() {
	}
	public ActionCreateAlarm(String alarmType, String alarmContent, String alarmLevel) {
		super();
		this.alarmType = alarmType;
		this.alarmContent = alarmContent;
		this.alarmLevel = alarmLevel;
	}
	
}
