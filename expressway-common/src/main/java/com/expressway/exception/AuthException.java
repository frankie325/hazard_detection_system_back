package com.expressway.exception;

/**
 * 认证相关异常（登录失败、令牌无效等）
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}