package com.expressway.vo;

import com.expressway.entity.DetectEventStream;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DetectEventStreamVO extends DetectEventStream {
    private Long areaId;        // 所属区域ID
    private Long deviceId;      // 所属设备ID
    private String location;    // 地点
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private String areaName;    // 所属区域名称
    private String deviceName;  // 所属设备名称
    private String eventTypeName; // 事件类型名称

    // 获取事件类型中文名称
    public String getEventTypeName() {
        if (getEventType() != null) {
            return getEventType().getDescription();
        }
        return null;
    }
}
