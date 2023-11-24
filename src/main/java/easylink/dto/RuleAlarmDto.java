package easylink.dto;

import easylink.entity.Alarm;
import easylink.entity.Rule;

public class RuleAlarmDto {
    Rule rule;
    ActionCreateAlarm alarm;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public ActionCreateAlarm getAlarm() {
        return alarm;
    }

    public void setAlarm(ActionCreateAlarm alarm) {
        this.alarm = alarm;
    }

    public RuleAlarmDto(Rule rule, ActionCreateAlarm alarm) {
        this.rule = rule;
        this.alarm = alarm;
    }
}
