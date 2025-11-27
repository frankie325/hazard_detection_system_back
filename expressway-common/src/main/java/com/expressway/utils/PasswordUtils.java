package com.expressway.utils;

import org.springframework.util.DigestUtils;
import java.util.UUID;

public class PasswordUtils {
    private static final String SALT_FIXED = "expressway_salt_";
    private static final String CHARSET = "UTF-8"; // 统一编码

    /**
     * 密码加密（MD5(明文+盐值)，指定UTF-8编码）
     */
    public static String encrypt(String password) {
        try {
            String salt = SALT_FIXED + UUID.randomUUID().toString().substring(0, 8);
            // 明确指定 UTF-8 编码
            byte[] inputBytes = (password + salt).getBytes(CHARSET);
            String encryptPwd = DigestUtils.md5DigestAsHex(inputBytes);
            return encryptPwd + ":" + salt;
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 密码验证（指定UTF-8编码）
     */
    public static boolean verify(String inputPwd, String dbEncryptPwd) {
        if (dbEncryptPwd == null || !dbEncryptPwd.contains(":")) {
            return false;
        }
        try {
            String[] parts = dbEncryptPwd.split(":", 2); // 限制拆分次数，避免盐值含 ":"（极端情况）
            if (parts.length != 2) {
                return false;
            }
            String dbPwd = parts[0];
            String salt = parts[1];
            // 明确指定 UTF-8 编码
            byte[] inputBytes = (inputPwd + salt).getBytes(CHARSET);
            String inputEncryptPwd = DigestUtils.md5DigestAsHex(inputBytes);
            return inputEncryptPwd.equals(dbPwd);
        } catch (Exception e) {
            throw new RuntimeException("密码验证失败", e);
        }
    }
}