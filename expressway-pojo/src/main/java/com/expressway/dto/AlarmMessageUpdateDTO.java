package com.expressway.dto;

import com.expressway.enumeration.AlarmStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlarmMessageUpdateDTO {
    @NotNull(message = "告警ID不能为空")
    private Long id;                    // 告警ID

    private AlarmStatus alarmStatus;    // 告警状态

    private String closeReason;         // 关闭原因

    private String processingResult;    // 处理结果

    private Long confirmedBy;           // 确认人ID
}
