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

    /**
     * 根据代码或描述获取枚举值
     */
    public static DetectEventType getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (DetectEventType type : values()) {
            if (type.code.equals(value) || type.description.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
