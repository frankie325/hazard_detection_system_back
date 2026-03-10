package com.expressway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeResourceBindVO {
    private Long id;                    // 主键ID
    private String resourceName;        // 资源名称
    private String resourceCode;        // 资源编码
    private String resourceType;       // 资源类型
    private Integer quantity;           // 资源数量
    private String status;              // 资源状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime boundAt;      // 绑定时间
    private String note;                // 备注
    private String departure;           // 出发地
    private String destination;         // 目的地
    private Integer bindCount;          // 绑定事件数
    private Integer updateCount;       // 更新次数
}