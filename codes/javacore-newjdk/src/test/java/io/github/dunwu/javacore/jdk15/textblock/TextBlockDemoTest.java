package io.github.dunwu.javacore.jdk15.textblock;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link TextBlockDemo} 单元测试。
 */
@DisplayName("Java 15 文本块示例测试")
public class TextBlockDemoTest {

    @Test
    @DisplayName("示例 1：JSON 文本块与传统拼接写法等价")
    public void testJsonTextBlock() {
        String output = captureOutput(TextBlockDemo::jsonTextBlock);
        assertThat(output)
            .contains("JSON 文本块与旧写法等价: true")
            .contains("\"name\": \"Java\"");
    }

    @Test
    @DisplayName("示例 2：SQL 文本块无需转义引号")
    public void testSqlTextBlock() {
        String output = captureOutput(TextBlockDemo::sqlTextBlock);
        assertThat(output)
            .contains("SQL 文本块:")
            .contains("WHERE age >= 18")
            .contains("AND name LIKE '%Java%'");
    }

    @Test
    @DisplayName("示例 3：HTML 文本块按结束标记去除公共缩进")
    public void testHtmlTextBlock() {
        String output = captureOutput(TextBlockDemo::htmlTextBlock);
        assertThat(output)
            .contains("HTML 文本块:")
            .contains("<p>Hello, 文本块</p>");
    }

    @Test
    @DisplayName("示例 4：行尾续行与 \\s 显式空格转义序列")
    public void testEscapeSequences() {
        String output = captureOutput(TextBlockDemo::escapeSequences);
        assertThat(output)
            .contains("续行结果: 红色 绿色 蓝色")
            .contains("每行补齐后的长度是否一致: true");
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
