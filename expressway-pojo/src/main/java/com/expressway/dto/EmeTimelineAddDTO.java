package com.expressway.dto;

import com.expressway.enumeration.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeTimelineAddDTO {
    @NotNull(message = "应急事件ID不能为空")
    private Long eventId;           // 应急事件ID

    private LocalDateTime operateTime;  // 发生时间（不传则使用当前时间）

    @NotNull(message = "操作类型不能为空")
    private ActionType actionType;      // 操作类型

    @NotBlank(message = "文本说明不能为空")
    private String actionText;          // 文本说明

    private String departure;           // 出发地

    private String destination;         // 目的地

    private String remark;              // 备注
}
