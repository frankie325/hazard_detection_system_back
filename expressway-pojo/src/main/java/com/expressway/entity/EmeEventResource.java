package com.expressway.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应急事件资源关联实体
 */
@Data
public class EmeEventResource {
    private Long id;                    // 主键ID
    private Long eventId;               // 应急事件ID
    private Long resourceId;            // 资源ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;   // 创建时间
}
