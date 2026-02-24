package com.expressway.dto;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.AlarmStatus;
import com.expressway.enumeration.DetectEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmMessageQueryParamsDTO extends PageDTO {
    private String alarmName;           // 告警名称
    private AlarmLevel alarmLevel;      // 告警等级
    private String deviceName;          // 设备名称
    private DetectEventType eventType;  // 告警分类
    private AlarmStatus alarmStatus;    // 告警状态
    private String startTime;           // 开始时间
    private String endTime;             // 结束时间
}
