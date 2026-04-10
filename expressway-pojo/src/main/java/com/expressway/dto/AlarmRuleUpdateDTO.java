package com.expressway.dto;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlarmRuleUpdateDTO {
    @NotNull(message = "规则ID不能为空")
    private Long id;

    private String ruleName;

    private DetectEventType eventType;

    private JsonNode matchCondition;

    private AlarmLevel alarmLevel;

    private Integer isEnabled;

    private String remark;
}
