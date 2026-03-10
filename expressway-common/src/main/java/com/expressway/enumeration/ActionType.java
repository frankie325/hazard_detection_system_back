package com.expressway.enumeration;

/**
 * 时间线操作类型枚举
 * 数据库中存储的是中文描述
 */
public enum ActionType {
    确认("CONFIRM", "确认"),
    资源绑定("BIND", "资源绑定"),
    现场签到("SIGN", "现场签到"),
    行动备注("REMARK", "行动备注"),
    上传资料("ATTACHMENT", "上传资料"),
    关闭事件("CLOSED", "关闭事件");

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
