package com.expressway.exception;

/**
 * 业务服务通用异常
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
