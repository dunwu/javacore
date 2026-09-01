package io.github.dunwu.javacore.spi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * spi 包示例测试：ServiceLoader 服务发现机制。
 */
@DisplayName("SPI 示例测试")
public class SpiDemoTest {

    @Test
    @DisplayName("SpiDemo：ServiceLoader 加载全部 DataStorage 实现")
    void testSpiDemo() {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            SpiDemo.demo();
        } finally {
            System.setOut(original);
        }
        String output = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        assertThat(output).contains("============ Java SPI 测试============");
        assertThat(output).contains("【Mysql】搜索Yes Or No，结果：No");
        assertThat(output).contains("【Redis】搜索Yes Or No，结果：Yes");
    }

}
