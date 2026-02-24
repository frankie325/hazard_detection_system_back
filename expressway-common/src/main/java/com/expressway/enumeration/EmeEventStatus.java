package com.expressway.enumeration;

/**
 * 应急事件状态枚举
 */
public enum EmeEventStatus {
    START("START", "启动"),
    CONFIRMED("CONFIRMED", "已确认"),
    DISPATCHING("DISPATCHING", "调度中"),
    PROCESSING("PROCESSING", "处理中"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String description;

    EmeEventStatus(String code, String description) {
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
