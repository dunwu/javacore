package io.github.dunwu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 模块冒烟测试
 */
public class AppTest {

    @Test
    @DisplayName("冒烟测试：验证测试环境可用")
    public void testApp() {
        Assertions.assertTrue(true);
    }

}
