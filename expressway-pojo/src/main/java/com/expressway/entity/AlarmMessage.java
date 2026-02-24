package com.expressway.entity;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.AlarmStatus;
import com.expressway.enumeration.DetectEventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmMessage {
    private Long id;                    // 告警ID
    private String alarmName;           // 告警名称
    private AlarmLevel alarmLevel;      // 告警等级
    private Long deviceId;              // 发生设备ID
    private DetectEventType eventType;  // 告警分类
    private Long ruleId;                // 规则来源ID
    private AlarmStatus alarmStatus;    // 告警状态
    private LocalDateTime closeTime;    // 关闭时间
    private String closeReason;         // 关闭原因
    private String processingResult;    // 处理结果
    private Long confirmedBy;           // 确认人ID
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
