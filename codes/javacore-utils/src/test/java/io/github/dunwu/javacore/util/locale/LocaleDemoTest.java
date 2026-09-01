package io.github.dunwu.javacore.util.locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * locale 包示例测试：DateFormat、MessageFormat、NumberFormat、ResourceBundle 本地化。
 */
@DisplayName("本地化示例测试")
public class LocaleDemoTest {

    @Test
    @DisplayName("DateFormatDemo：同一日期在不同地区下的格式差异")
    void testDateFormatDemo() {
        String output = captureOutput(DateFormatDemo::demo);
        assertThat(output).contains("的本地化（en）结果: ");
        assertThat(output).contains("的本地化（zh_CN）结果: ");
    }

    @Test
    @DisplayName("LoaleDemo：ResourceBundle 按 Locale 加载多语言文本")
    void testLoaleDemo() {
        String output = captureOutput(LoaleDemo::demo);
        assertThat(output).contains("us-US:HelloWorld!");
        assertThat(output).contains("us-US:The current time is 08:00.");
        assertThat(output).contains("zh-CN：世界，你好！");
        assertThat(output).contains("zh-CN：当前时间是08:00。");
        assertThat(output).contains("default：");
    }

    @Test
    @DisplayName("MessageFormatDemo：占位符模板拼接消息")
    void testMessageFormatDemo() {
        String output = captureOutput(MessageFormatDemo::demo);
        assertThat(output).contains("Jack，你好！");
        assertThat(output).contains("消费");
        assertThat(output).contains("paid");
    }

    @Test
    @DisplayName("NumberFormatDemo：按地区格式化货币")
    void testNumberFormatDemo() {
        String output = captureOutput(NumberFormatDemo::demo);
        assertThat(output).contains("的本地化（zh_CN）结果: ");
        // 简体中文货币符号与金额部分
        assertThat(output).contains("123,456.78");
    }

    /**
     * 捕获 System.out 输出。
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
