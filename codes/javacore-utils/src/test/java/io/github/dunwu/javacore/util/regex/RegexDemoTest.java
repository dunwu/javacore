package io.github.dunwu.javacore.util.regex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * regex 包示例测试：正则验证、拆分、替换与 RegexUtil 工具类。
 */
@DisplayName("正则表达式示例测试")
public class RegexDemoTest {

    @Test
    @DisplayName("RegexDemo01：逐字符判断是否全为数字（不用正则）")
    void testRegexDemo01() {
        String output = captureOutput(RegexDemo01::demo);
        assertThat(output).contains("是由数字组成！");
    }

    @Test
    @DisplayName("RegexDemo02：一行正则判断是否全为数字")
    void testRegexDemo02() {
        String output = captureOutput(RegexDemo02::demo);
        assertThat(output).contains("是由数字组成！");
    }

    @Test
    @DisplayName("RegexDemo03：Pattern + Matcher 验证日期格式")
    void testRegexDemo03() {
        String output = captureOutput(RegexDemo03::demo);
        assertThat(output).contains("日期格式合法！");
    }

    @Test
    @DisplayName("RegexDemo04：Pattern.split() 按数字拆分取字母")
    void testRegexDemo04() {
        String output = captureOutput(RegexDemo04::demo);
        assertThat(output).contains("A\tB\tC\tD\tE\tF");
    }

    @Test
    @DisplayName("RegexDemo05：Matcher.replaceAll() 把数字替换为下划线")
    void testRegexDemo05() {
        String output = captureOutput(RegexDemo05::demo);
        assertThat(output).contains("A_B_C_D_E_F");
    }

    @Test
    @DisplayName("RegexDemo06：String 内置的正则快捷方法")
    void testRegexDemo06() {
        String output = captureOutput(RegexDemo06::demo);
        assertThat(output).contains("字符串替换操作：A_B_C_D_E_F");
        assertThat(output).contains("字符串验证：true");
        assertThat(output).contains("字符串的拆分：A\tB");
    }

    @Test
    @DisplayName("RegexDemo07：多级拆分解析键值对")
    void testRegexDemo07() {
        String output = captureOutput(RegexDemo07::demo);
        assertThat(output).contains("字符串的拆分：");
        assertThat(output).contains("LXH\t98");
        assertThat(output).contains("JAVA\t90");
        assertThat(output).contains("LI\t100");
    }

    @Test
    @DisplayName("RegexUtil.demo：常用校验方法演示")
    void testRegexUtilDemo() {
        String output = captureOutput(RegexUtil::demo);
        assertThat(output).contains("127.0.0.1 是合法 IPv4：true");
        assertThat(output).contains("10.10.10.256 是合法 IPv4：false");
        assertThat(output).contains("forbreak@163.com 是合法邮箱：true");
        assertThat(output).contains("15812345678 是合法手机号：true");
    }

    @Test
    @DisplayName("RegexUtil：IPv4 校验")
    void testIpv4() {
        assertThat(RegexUtil.isValidateIpv4("0.0.0.0")).isTrue();
        assertThat(RegexUtil.isValidateIpv4("255.255.255.255")).isTrue();
        assertThat(RegexUtil.isValidateIpv4("127.0.0.1")).isTrue();
        assertThat(RegexUtil.isValidateIpv4("10.10.10")).isFalse();
        assertThat(RegexUtil.isValidateIpv4("10.10.10.256")).isFalse();
    }

    @Test
    @DisplayName("RegexUtil：邮箱、手机号、日期、时间校验")
    void testCommonValidation() {
        assertThat(RegexUtil.isValidateEmail("he_llo@worl.d.com")).isTrue();
        assertThat(RegexUtil.isValidateEmail("he&llo@world.co1")).isFalse();
        assertThat(RegexUtil.isValidateMobile("15812345678")).isTrue();
        assertThat(RegexUtil.isValidateMobile("15412345678")).isFalse();
        assertThat(RegexUtil.isValidateDate("2016-01-01")).isTrue();
        assertThat(RegexUtil.isValidateDate("2001-02-29")).isFalse();
        assertThat(RegexUtil.isValidTime("23:59:59")).isTrue();
        assertThat(RegexUtil.isValidTime("24:16:30")).isFalse();
    }

    @Test
    @DisplayName("RegexUtil：数字与字符类校验")
    void testCharValidation() {
        assertThat(RegexUtil.isAllNumber("123456")).isTrue();
        assertThat(RegexUtil.isAllNumber("12a456")).isFalse();
        assertThat(RegexUtil.isNDigitNumber("123456", 6)).isTrue();
        assertThat(RegexUtil.isNDigitNumber("12345", 6)).isFalse();
        assertThat(RegexUtil.isLeastNDigitNumber("123456", 4)).isTrue();
        assertThat(RegexUtil.isMToNDigitNumber("123456", 4, 8)).isTrue();
        assertThat(RegexUtil.isAllEnglishChar("Hello")).isTrue();
        assertThat(RegexUtil.isAllUpperEnglishChar("HELLO")).isTrue();
        assertThat(RegexUtil.isAllLowerEnglishChar("hello")).isTrue();
        assertThat(RegexUtil.isAllWordChar("hello_123")).isTrue();
        assertThat(RegexUtil.isAllChineseChar("春眠不觉晓")).isTrue();
        assertThat(RegexUtil.isAllChineseChar("春眠不觉晓，")).isFalse();
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
