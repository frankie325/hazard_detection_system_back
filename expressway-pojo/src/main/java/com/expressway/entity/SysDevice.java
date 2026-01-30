package com.expressway.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysDevice {
    private Long id;              // 设备ID
    private String deviceName;    // 设备名称（必填）
    private String deviceCode;    // 设备编码（必填）
    private String deviceType;    // 设备类型（摄像头/传感器等）
    private String deviceModel;   // 设备型号
    private Long areaId;          // 所属区域ID
    private String location;      // 安装位置
    private String ipAddress;     // IP地址
    private String status;        // 状态（在线/离线/维护）
    private Date createTime;      // 创建时间
    private Date updateTime;      // 更新时间
}
