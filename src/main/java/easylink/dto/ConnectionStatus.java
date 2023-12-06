package easylink.dto;

import java.io.Serializable;

/**
 * Store & monitor connection status & sent alarm for connection in redis
 */
public class ConnectionStatus implements Serializable {
    public static final Integer NOK = 0;
    public static final Integer OK = 1;

    int status = 0;
    Long lastEventTime;
    Long lastAlarmTime;
    boolean sentLostConnectionAlarm = false;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(Long lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public Long getLastAlarmTime() {
        return lastAlarmTime;
    }

    public void setLastAlarmTime(Long lastAlarmTime) {
        this.lastAlarmTime = lastAlarmTime;
    }

    public boolean isSentLostConnectionAlarm() {
        return sentLostConnectionAlarm;
    }

    public void setSentLostConnectionAlarm(boolean sentLostConnectionAlarm) {
        this.sentLostConnectionAlarm = sentLostConnectionAlarm;
    }

    @Override
    public String toString() {
        return "ConnectionStatus{" +
                "status=" + status +
                ", lastEventTime=" + lastEventTime +
                ", lastAlarmTime=" + lastAlarmTime +
                ", sentLostConnectionAlarm=" + sentLostConnectionAlarm +
                '}';
    }
}
