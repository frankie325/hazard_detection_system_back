package com.expressway.dto;

import com.expressway.enumeration.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增设备入参DTO
 */
@Data
public class DeviceAddDTO {
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 32, message = "设备名称长度不能超过32个字符")
    private String deviceName;    // 设备名称（必填）

    @NotBlank(message = "设备编码不能为空")
    private String deviceCode;    // 设备编码（必填）

    @NotNull(message = "设备类型不能为空")
    private DeviceType deviceType;    // 设备类型（必填）

    private String model;   // 设备型号（非必填）

    @NotNull(message = "所属区域不能为空")
    private Long areaId;          // 所属区域ID（必填）

    private String location;      // 安装位置（非必填）

    private String ipAddress;     // IP地址（非必填）

    private Long alarmRuleId;     // 应用规则ID（非必填）
}
