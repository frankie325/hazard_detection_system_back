package com.expressway.enumeration;

/**
 * 告警状态枚举
 */
public enum AlarmStatus {
    OPEN("OPEN", "开启"),
    PROCESSING("PROCESSING", "处理中"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String description;

    AlarmStatus(String code, String description) {
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
