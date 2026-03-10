package com.expressway.enumeration;

/**
 * 应急事件状态枚举
 * 数据库中存储的是中文描述
 */
public enum EmeEventStatus {
    启动("START", "启动"),
    已确认("CONFIRMED", "已确认"),
    调度中("DISPATCHING", "调度中"),
    处理中("PROCESSING", "处理中"),
    已关闭("CLOSED", "已关闭");

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
