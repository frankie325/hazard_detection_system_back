package com.expressway.utils;

import com.expressway.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT令牌工具类
 */
public class JwtUtils {

    /**
     * 生成令牌（传入用户名作为核心标识）
     */
    public static String generateToken(String secretKey, String username) {
        return Jwts.builder()
                .setSubject(username) // 存储用户名
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + AuthConstant.TOKEN_EXPIRE)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8)) // 签名加密
                .compact();
    }

    /**
     * 生成JWT令牌（支持自定义claims）
     * @param secretKey jwt秘钥
     * @param claims 设置的信息
     * @return
     */
    public static String generateToken(String secretKey, Map<String, Object> claims) {
        long expMillis = System.currentTimeMillis() + AuthConstant.TOKEN_EXPIRE;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setClaims(claims) // 设置自定义claims
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(exp) // 过期时间
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 生成JWT令牌（支持自定义claims和过期时间）
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims 设置的信息
     * @return
     */
    public static String generateToken(String secretKey, long ttlMillis, Map<String, Object> claims) {
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 验证令牌有效性（是否过期、签名是否正确）
     */
    public static boolean validateToken(String secretKey, String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中获取用户名
     */
    public static String getUsernameFromToken(String secretKey, String token) {
        Claims claims = parseToken(secretKey, token);
        return claims.getSubject();
    }

    /**
     * 从令牌中获取所有claims信息
     */
    public static Claims parseToken(String secretKey, String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取指定claim的值
     */
    public static <T> T getClaimFromToken(String secretKey, String token, String claimName, Class<T> clazz) {
        Claims claims = parseToken(secretKey, token);
        return claims.get(claimName, clazz);
    }

    /**
     * 获取令牌过期时间
     */
    public static Date getExpirationFromToken(String secretKey, String token) {
        Claims claims = parseToken(secretKey, token);
        return claims.getExpiration();
    }

    /**
     * 检查令牌是否过期
     */
    public static boolean isTokenExpired(String secretKey, String token) {
        Date expiration = getExpirationFromToken(secretKey, token);
        return expiration.before(new Date());
    }
}