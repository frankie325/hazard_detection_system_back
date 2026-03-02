package com.expressway.enumeration;

/**
 * 时间线操作类型枚举
 */
public enum ActionType {
    CONFIRM("CONFIRM", "确认"),
    BIND("BIND", "资源绑定"),
    SIGN("SIGN", "现场签到"),
    REMARK("REMARK", "行动备注"),
    ATTACHMENT("ATTACHMENT", "上传资料"),
    CLOSED("CLOSED", "关闭事件");

    private final String code;
    private final String description;

    ActionType(String code, String description) {
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
