package com.expressway.entity;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AlarmRule {
    private Long id;                    // 规则ID
    private String ruleName;            // 规则名称
    private DetectEventType eventType; // 危害类型
    private String matchCondition;      // 匹配条件(JSON格式)
    private AlarmLevel alarmLevel;      // 告警等级
    private Integer isEnabled;          // 是否启用(1-是, 0-否)
    private String remark;              // 备注
    private LocalDateTime createTime;            // 创建时间
    private LocalDateTime updateTime;            // 更新时间
}
