package com.expressway.dto;

import com.expressway.enumeration.DeviceStatus;
import com.expressway.enumeration.DeviceType;
import lombok.Data;

@Data
public class DeviceQueryParamsDTO extends PageDTO {
    private String deviceName;
    private String deviceCode;
    private DeviceType deviceType;
    private DeviceStatus status;
    private Long areaId;
}
