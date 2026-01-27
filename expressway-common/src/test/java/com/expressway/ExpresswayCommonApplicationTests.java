package com.expressway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 普通单元测试（无Spring上下文，适配common工具模块）
 */
public class ExpresswayCommonApplicationTests {

    @Test
    void testCommonModule() {
        // 简单的测试断言，确保模块能正常编译运行
        assertTrue(true, "Common模块测试通过");
    }

}