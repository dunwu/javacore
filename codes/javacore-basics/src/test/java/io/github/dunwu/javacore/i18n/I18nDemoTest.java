package io.github.dunwu.javacore.i18n;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * i18n 包示例测试：DateFormat、MessageFormat、NumberFormat、ResourceBundle 国际化。
 */
@DisplayName("国际化示例测试")
public class I18nDemoTest {

    @Test
    @DisplayName("DateFormatDemo：日期在不同地区下的格式差异")
    void testDateFormatDemo() {
        String output = captureOutput(DateFormatDemo::demo);
        assertThat(output).contains("的国际化（en）结果: ");
        assertThat(output).contains("的国际化（zh_CN）结果: ");
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
        assertThat(output).contains("的国际化（zh_CN）结果: ");
        assertThat(output).contains("123,456.78");
    }

    @Test
    @DisplayName("ResourceBundleDemo：ResourceBundle 按 Locale 加载多语言文本")
    void testResourceBundleDemo() {
        String output = captureOutput(ResourceBundleDemo::demo);
        assertThat(output).contains("en-US:HelloWorld!");
        assertThat(output).contains("en-US:The current time is 08:00.");
        assertThat(output).contains("zh-CN：世界，你好！");
        assertThat(output).contains("zh-CN：当前时间是08:00。");
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
