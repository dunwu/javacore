package io.github.dunwu.javacore.util.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * math 包示例测试：BigDecimal、BigInteger、Math、NumberFormat、Random。
 */
@DisplayName("数学计算示例测试")
public class MathDemoTest {

    @Test
    @DisplayName("BigDecimalDemo01：BigDecimal 精确四则运算与四舍五入")
    void testBigDecimalDemo01() {
        String output = captureOutput(BigDecimalDemo01::demo);
        assertThat(output).contains("加法运算：13.7");
        assertThat(output).contains("减法运算：7.012");
        assertThat(output).contains("乘法运算：34.48");
        assertThat(output).contains("除法运算：3.104");
    }

    @Test
    @DisplayName("BigIntegerDemo01：BigInteger 大整数运算")
    void testBigIntegerDemo01() {
        String output = captureOutput(BigIntegerDemo01::demo);
        assertThat(output).contains("加法操作：1111111110");
        assertThat(output).contains("减法操作：864197532");
        assertThat(output).contains("乘法操作：121932631112635269");
        assertThat(output).contains("除法操作：8");
        assertThat(output).contains("最大数：987654321");
        assertThat(output).contains("最小数：123456789");
        assertThat(output).contains("商是：8；余数是：9");
    }

    @Test
    @DisplayName("MathDemo01：Math 常用数学运算")
    void testMathDemo01() {
        String output = captureOutput(MathDemo01::demo);
        assertThat(output).contains("求平方根：3.0");
        assertThat(output).contains("求两数的最大值：30");
        assertThat(output).contains("求两数的最小值：10");
        assertThat(output).contains("2的3次方：8.0");
        assertThat(output).contains("四舍五入：34");
    }

    @Test
    @DisplayName("NumberFormatDemo01：默认地区数字格式化")
    void testNumberFormatDemo01() {
        String output = captureOutput(NumberFormatDemo01::demo);
        assertThat(output).contains("格式化之后的数字：");
    }

    @Test
    @DisplayName("NumberFormatDemo02：DecimalFormat 自定义模板")
    void testNumberFormatDemo02() {
        String output = captureOutput(NumberFormatDemo02::demo);
        assertThat(output).contains("使用###,###.###格式化数字111222.34567：111,222.346");
        assertThat(output).contains("使用000,000.000格式化数字11222.34567：011,222.346");
        assertThat(output).contains("111,222.346￥");
        assertThat(output).contains("34.568%");
    }

    @Test
    @DisplayName("RandomDemo01：生成 10 个 [0, 100) 的随机整数")
    void testRandomDemo01() {
        String output = captureOutput(RandomDemo01::demo);
        String[] parts = output.trim().split("\t");
        assertThat(parts).hasSize(10);
        for (String part : parts) {
            int num = Integer.parseInt(part.trim());
            assertThat(num).isBetween(0, 99);
        }
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
