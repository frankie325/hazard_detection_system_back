package com.expressway.entity;

import com.expressway.enumeration.DeviceStatus;
import com.expressway.enumeration.DeviceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SysDevice {
    private Long id;              // 设备ID
    private String deviceName;    // 设备名称（必填）
    private String deviceCode;    // 设备编码（必填）
    private DeviceType deviceType;    // 设备类型（摄像头/传感器等）
    private String model;   // 设备型号
    private Long areaId;          // 所属区域ID
    private String location;      // 安装位置
    private String ipAddress;     // IP地址
    private Long alarmRuleId;     // 应用规则ID
    private DeviceStatus status;        // 状态（在线/离线/维护）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;      // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;      // 更新时间
}
