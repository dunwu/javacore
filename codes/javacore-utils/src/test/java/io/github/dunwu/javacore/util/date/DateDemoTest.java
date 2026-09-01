package io.github.dunwu.javacore.util.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * date 包示例测试：Date、Calendar、DateFormat、SimpleDateFormat。
 */
@DisplayName("日期时间示例测试")
public class DateDemoTest {

    @Test
    @DisplayName("DateDemo01：取得当前日期")
    void testDateDemo01() {
        String output = captureOutput(DateDemo01::demo);
        assertThat(output).contains("当前日期为：");
    }

    @Test
    @DisplayName("DateDemo02：Calendar 按字段取得日期时间")
    void testDateDemo02() {
        String output = captureOutput(DateDemo02::demo);
        assertThat(output).contains("YEAR: ");
        assertThat(output).contains("MONTH: ");
        assertThat(output).contains("DAY_OF_MONTH: ");
        assertThat(output).contains("HOUR_OF_DAY: ");
        assertThat(output).contains("MINUTE: ");
        assertThat(output).contains("SECOND: ");
        assertThat(output).contains("MILLISECOND: ");
    }

    @Test
    @DisplayName("DateDemo03：DateFormat 默认地区格式化")
    void testDateDemo03() {
        String output = captureOutput(DateDemo03::demo);
        assertThat(output).contains("DATE：");
        assertThat(output).contains("DATETIME：");
    }

    @Test
    @DisplayName("DateDemo04：DateFormat 指定地区格式化")
    void testDateDemo04() {
        String output = captureOutput(DateDemo04::demo);
        assertThat(output).contains("DATE：");
        assertThat(output).contains("DATETIME：");
    }

    @Test
    @DisplayName("DateDemo05：SimpleDateFormat 提取并转换日期格式")
    void testDateDemo05() {
        String output = captureOutput(DateDemo05::demo);
        assertThat(output).contains("2008年10月19日 10时11分30秒345毫秒");
    }

    @Test
    @DisplayName("DateDemo06：手工拼接日期字符串（补零）")
    void testDateDemo06() {
        String output = captureOutput(DateDemo06::demo);
        assertThat(output).contains("系统日期：");
        assertThat(output).contains("中文日期：");
        assertThat(output).contains("时间戳：");
        // 中文格式必然以"毫秒"结尾
        assertThat(output).contains("毫秒");
    }

    @Test
    @DisplayName("DateDemo07：SimpleDateFormat 格式化三种日期格式")
    void testDateDemo07() {
        String output = captureOutput(DateDemo07::demo);
        assertThat(output).contains("系统日期：");
        assertThat(output).contains("中文日期：");
        assertThat(output).contains("时间戳：");
        // 时间戳是 17 位纯数字：yyyyMMddHHmmssSSS
        String timestamp = output.substring(output.indexOf("时间戳：") + 4).trim();
        assertThat(timestamp).matches("\\d{17}");
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
