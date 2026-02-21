package com.expressway.exception;

/**
 * 告警规则相关业务异常
 */
public class AlarmRuleException extends RuntimeException {
    public AlarmRuleException(String message) {
        super(message);
    }
}
