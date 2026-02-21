package com.expressway.exception;

/**
 * 用户相关业务异常
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
