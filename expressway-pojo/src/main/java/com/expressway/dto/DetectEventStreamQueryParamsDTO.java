package com.expressway.dto;

import com.expressway.enumeration.DetectEventType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DetectEventStreamQueryParamsDTO extends PageDTO {
    private String eventName;       // 事件名称
    private Long deviceId;          // 设备ID
    private String deviceName;      // 设备名称
    private String areaName;        // 区域名称
    private DetectEventType eventType; // 事件类型

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;         // 开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;           // 结束时间
}
