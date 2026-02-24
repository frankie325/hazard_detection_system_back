package com.expressway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlarmConfirmDTO {
    @NotNull(message = "部门ID不能为空")
    private Long deptId;
    @NotNull(message = "告警消息ID不能为空")
    private Long alarmId;
}
