package com.expressway.enumeration;

/**
 * 设备状态枚举
 */
public enum DeviceStatus {
    ONLINE("ONLINE", "在线"),
    OFFLINE("OFFLINE", "离线"),
    MAINTENANCE("MAINTENANCE", "维护");

    private final String code;
    private final String description;

    DeviceStatus(String code, String description) {
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
