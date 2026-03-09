package com.expressway.enumeration;

/**
 * 告警等级枚举
 */
public enum AlarmLevel {
    LOW("L", "低级"),
    MEDIUM("M", "中级"),
    HIGH("H", "高级"),
    EMERGENCY("E", "紧急");

    private final String code;
    private final String description;

    AlarmLevel(String code, String description) {
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
