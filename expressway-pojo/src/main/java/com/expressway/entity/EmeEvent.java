package com.expressway.entity;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.expressway.enumeration.EmeEventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeEvent {
    private Long id;                    // 事件ID
    private String eventName;           // 事件名称
    private AlarmLevel eventLevel;      // 事件等级
    private DetectEventType eventType;  // 事件类型
    private String location;            // 地点
    private EmeEventStatus status;      // 状态
    private Long receiverRoleId;            // 接收角色ID
    private Long alarmId;               // 关联告警ID
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
    private Long deptId;                // 处置部门id
}
