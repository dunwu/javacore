package io.github.dunwu.javacore.jdk8.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link DateTimeFormatterDemo} 单元测试。
 */
@DisplayName("Java 8 DateTimeFormatter 格式化示例测试")
public class DateTimeFormatterDemoTest {

    @Test
    @DisplayName("示例 1：内置 ISO 格式输出")
    public void testIsoFormat() {
        String output = captureOutput(DateTimeFormatterDemo::isoFormat);
        assertThat(output)
            .contains("ISO_LOCAL_DATE_TIME: 2024-05-20T15:30:45")
            .contains("ISO_LOCAL_DATE: 2024-05-20");
    }

    @Test
    @DisplayName("示例 2：自定义 pattern 与中文格式")
    public void testCustomPattern() {
        String output = captureOutput(DateTimeFormatterDemo::customPattern);
        assertThat(output)
            .contains("自定义格式化: 2024-05-20 15:30:45")
            .contains("中文格式: 2024年05月20日 15时30分");
    }

    @Test
    @DisplayName("示例 3：固定 Locale.CHINA 的本地化 MEDIUM 风格")
    public void testLocalizedStyle() {
        String output = captureOutput(DateTimeFormatterDemo::localizedStyle);
        assertThat(output).contains("MEDIUM 风格: 2024年5月20日 15:30:45");
    }

    @Test
    @DisplayName("示例 4：自定义 pattern 与默认 ISO 格式解析")
    public void testParseDemo() {
        String output = captureOutput(DateTimeFormatterDemo::parseDemo);
        assertThat(output)
            .contains("解析结果: 2024-05-20T15:30:45")
            .contains("ISO 解析: 2024-05-20T15:30:45");
    }

    @Test
    @DisplayName("示例 5：format 与 parse 往返互逆")
    public void testRoundTrip() {
        String output = captureOutput(DateTimeFormatterDemo::roundTrip);
        assertThat(output).contains("format -> parse 往返一致: true");
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
