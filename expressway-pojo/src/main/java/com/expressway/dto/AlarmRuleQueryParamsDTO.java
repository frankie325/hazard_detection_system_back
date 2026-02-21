package com.expressway.dto;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import lombok.Data;

@Data
public class AlarmRuleQueryParamsDTO extends PageDTO {
    private Short isEnabled;
    private String ruleName;            // 规则名称
    private DetectEventType eventType; // 危害类型
    private AlarmLevel alarmLevel;      // 告警等级
}
