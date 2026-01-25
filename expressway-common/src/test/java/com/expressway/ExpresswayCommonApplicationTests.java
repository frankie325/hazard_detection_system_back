package com.expressway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 普通单元测试（无Spring上下文）
 */
public class ExpresswayCommonApplicationTests {

    @Test
    void testCommonModule() {
        assertTrue(true, "Common模块测试通过");
    }

}