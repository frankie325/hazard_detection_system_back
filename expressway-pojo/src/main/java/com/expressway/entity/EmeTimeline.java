package com.expressway.entity;

import com.expressway.enumeration.ActionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeTimeline {
    private Long id;                    // 时间线ID
    private Long eventId;               // 应急事件ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateTime;  // 发生时间
    private Long operatorId;            // 执行者ID
    private ActionType actionType;      // 操作类型
    private String actionText;          // 文本说明
    private String departure;           // 出发地
    private String destination;         // 目的地
    private String remark;              // 备注
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;   // 创建时间
}
