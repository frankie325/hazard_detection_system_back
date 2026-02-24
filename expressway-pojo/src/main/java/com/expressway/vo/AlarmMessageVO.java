package com.expressway.vo;

import com.expressway.entity.AlarmMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmMessageVO extends AlarmMessage {
    private String deviceName;
    private String location;
    private String ruleName;
    private String alarmLevelName;

    // 获取告警等级中文名称
    public String getAlarmLevelName() {
        if (getAlarmLevel() != null) {
            return getAlarmLevel().getDescription();
        }
        return null;
    }

    // 获取告警状态中文名称
    public String getAlarmStatusName() {
        if (getAlarmStatus() != null) {
            return getAlarmStatus().getDescription();
        }
        return null;
    }

    // 获取告警分类中文名称
    public String getEventTypeName() {
        if (getEventType() != null) {
            return getEventType().getDescription();
        }
        return null;
    }
}
