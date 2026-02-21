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

    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @NotNull(message = "危害类型不能为空")
    private DetectEventType eventType;

    @NotNull(message = "匹配条件不能为空")
    private JsonNode matchCondition;

    @NotNull(message = "告警等级不能为空")
    private AlarmLevel alarmLevel;

    private Integer isEnabled;

    private String remark;
}
