package com.expressway.vo;

import com.expressway.entity.SysDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceVO extends SysDevice {
    private String deviceTypeName; // 设备类型名称
    private String statusName; // 设备状态名称
    private String areaName;  // 所属区域名称

    // 获取设备类型中文名称
    public String getDeviceTypeName() {
        if (getDeviceType() != null) {
            return getDeviceType().getDescription();
        }
        return null;
    }

    // 获取状态中文名称
    public String getStatusName() {
        if (getStatus() != null) {
            return getStatus().getDescription();
        }
        return null;
    }
}
