package com.expressway;

import com.expressway.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class PasswordTest {

    @Test
    void testEncrypt() throws UnsupportedEncodingException {
        // 1. 从数据库提取的信息
        String inputPwd = "123456"; // 前端输入明文

        // 2. 调用PasswordUtils.encrypt得到加密后的密码
        System.out.println(PasswordUtils.encrypt(inputPwd));

    }
}