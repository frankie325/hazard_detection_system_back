package com.expressway.dto;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.expressway.enumeration.EmeEventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmeEventAddDTO {
    @NotBlank(message = "事件名称不能为空")
    private String eventName;           // 事件名称

    @NotNull(message = "事件等级不能为空")
    private AlarmLevel eventLevel;      // 事件等级

    @NotNull(message = "事件类型不能为空")
    private DetectEventType eventType;  // 事件类型
    @NotNull(message = "事件状态不能为空")
    private String location;            // 地点

    private Long alarmId;               // 关联告警ID
    private Long deptId;            // 处置部门id
}
