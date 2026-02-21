package com.expressway.dto;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlarmRuleAddDTO {
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;            // 规则名称

    @NotNull(message = "危害类型不能为空")
    private DetectEventType eventType; // 危害类型

    @NotNull(message = "匹配条件不能为空")
    private JsonNode matchCondition;    // 匹配条件(JSON对象)

    @NotNull(message = "告警等级不能为空")
    private AlarmLevel alarmLevel;      // 告警等级

    private Integer isEnabled = 1;      // 是否启用(1-是, 0-否)，默认启用

    private String remark;              // 备注
}
