package com.expressway.enumeration;

/**
 * 检测事件类型枚举
 */
public enum DetectEventType {
    CAST("CAST", "抛洒物"),
    FIRE("FIRE", "火灾"),
    LANDSLIDE("LANDSLIDE", "塌方"),
    TRAFFIC_ACCIDENT("TRAFFIC_ACCIDENT", "交通事故");

    private final String code;
    private final String description;

    DetectEventType(String code, String description) {
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
