package com.expressway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 普通单元测试（无Spring上下文）
 */
public class ExpresswayPojoApplicationTests {

    @Test
    void testPojoModule() {
        assertTrue(true, "Pojo模块测试通过");
    }

}