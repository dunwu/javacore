package io.github.dunwu.javacore.jdk11.string;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StringEnhanceDemo} 单元测试。
 */
@DisplayName("Java 11 String 增强示例测试")
public class StringEnhanceDemoTest {

    @Test
    @DisplayName("示例 1：isBlank 判断空白与 lines 按行分割")
    public void testIsBlankAndLines() {
        String output = captureOutput(StringEnhanceDemo::isBlankAndLines);
        assertThat(output)
            .contains("\"\".isBlank(): true")
            .contains("\"a\".isBlank(): false")
            .contains("lines: [第一行, 第二行, 第三行]");
    }

    @Test
    @DisplayName("示例 2：strip 系列支持 Unicode 空白字符")
    public void testStripMethods() {
        String output = captureOutput(StringEnhanceDemo::stripMethods);
        assertThat(output)
            .contains("strip 结果: [前后有空格]")
            .contains("stripLeading 结果: [前后有空格\u2000]")
            .contains("stripTrailing 结果: [\u2000前后有空格]");
    }

    @Test
    @DisplayName("示例 3：repeat 重复字符串与组合 strip 清洗输入")
    public void testRepeatAndClean() {
        String output = captureOutput(StringEnhanceDemo::repeatAndClean);
        assertThat(output)
            .contains("repeat 结果: Java Java Java ")
            .contains("清洗结果: [hello world]");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

}
