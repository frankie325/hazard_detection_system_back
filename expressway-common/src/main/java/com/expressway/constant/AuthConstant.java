package com.expressway.constant;

/**
 * 认证相关常量
 */
public class AuthConstant {
    // JWT令牌前缀（前端请求头携带格式：Bearer xxx）
    public static final String TOKEN_PREFIX = "Bearer ";
    // 请求头中令牌的key
    public static final String TOKEN_HEADER = "Authorization";
    // JWT密钥（可移至properties配置，此处先定义常量）
    public static final String JWT_SECRET = "expressway_2024_emergency_system_secret_key";
    // 令牌有效期（2小时，单位：毫秒）
    public static final long TOKEN_EXPIRE = 7200000L;
}