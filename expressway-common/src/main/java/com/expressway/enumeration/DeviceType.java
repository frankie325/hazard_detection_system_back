package com.expressway.enumeration;

/**
 * 设备类型枚举
 */
public enum DeviceType {
    CAMERA("CAMERA", "摄像头"),
    SENSOR("SENSOR", "传感器");

    private final String code;
    private final String description;

    DeviceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
