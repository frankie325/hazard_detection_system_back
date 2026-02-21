package com.expressway.vo;

import com.expressway.entity.SysArea;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 区域设备VO - 包含区域及其下的设备列表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaDeviceVO extends SysArea {
    private List<DeviceVO> deviceList;    // 设备列表
}
