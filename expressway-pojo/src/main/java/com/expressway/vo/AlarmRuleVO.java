package com.expressway.vo;

import com.expressway.entity.AlarmRule;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmRuleVO extends AlarmRule {
    private String hazardTypeName; // 危害类型名称
    private String alarmLevelName;  // 告警等级名称

    // 获取危害类型中文名称
    public String getHazardTypeName() {
        if (getHazardType() != null) {
            return getHazardType().getDescription();
        }
        return null;
    }

    // 获取告警等级中文名称
    public String getAlarmLevelName() {
        if (getAlarmLevel() != null) {
            return getAlarmLevel().getDescription();
        }
        return null;
    }
}
