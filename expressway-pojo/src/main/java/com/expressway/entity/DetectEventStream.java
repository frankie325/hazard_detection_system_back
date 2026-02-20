package com.expressway.entity;

import com.expressway.enumeration.DetectEventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DetectEventStream {
    private Long id;            // 事件流id
    private String eventName;      // 事件流名称
    private DetectEventType eventType;      // 事件流类型
    private Float confidence;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;    // 创建时间
}
