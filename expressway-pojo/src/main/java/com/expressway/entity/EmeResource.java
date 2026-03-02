package com.expressway.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeResource {
    private Long id;                    // 主键ID
    private String resourceName;        // 资源名称
    private Integer num;                // 资源数量
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;   // 创建时间
}
