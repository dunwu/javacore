package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * datatype 包数据类型示例单元测试
 * <p>
 * 注：StringIntern性能测试 为性能基准（1000 万次 intern），不在单元测试中运行
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class DataTypeDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("BigDecimal判等：equals 比较精度，compareTo 只比较数值")
    void testBigDecimal判等() {
        String output = captureOutput(BigDecimal判等::demo);
        assertThat(output).isEqualTo("====================== wrong ======================\n"
            + "false\n"
            + "====================== right ======================\n"
            + "true\n"
            + "====================== set ======================\n"
            + "false\n"
            + "true\n"
            + "true\n");
    }

    @Test
    @DisplayName("String拼接：常量拼接被编译器优化")
    void testString拼接() {
        String output = captureOutput(String拼接::demo);
        assertThat(output).isEqualTo("str = abc\n");
    }

    @Test
    @DisplayName("值类型使用示例：反射依次执行 demo1~demo10")
    void test值类型使用示例() {
        String output = captureOutput(值类型使用示例::demo);
        assertThat(output).contains("整型的最大值：2147483647");
        assertThat(output).contains("整型的最大值 + 1：-2147483648");
        assertThat(output).contains("ch1 = a");
        assertThat(output).contains("两个小数相乘：9.0");
        assertThat(output).contains("1 + 2 = 12");
        assertThat(output).contains("1 + 2 = 3");
    }

    @Test
    @DisplayName("包装类型使用示例：打印各包装类的位数与取值范围")
    void test包装类型使用示例() {
        String output = captureOutput(包装类型使用示例::demo);
        assertThat(output).contains("基本类型：byte 二进制位数：8");
        assertThat(output).contains("最小值：Integer.MIN_VALUE=-2147483648");
        assertThat(output).contains("最大值：Long.MAX_VALUE=9223372036854775807");
        assertThat(output).contains("包装类：java.lang.Double");
    }

    @Test
    @DisplayName("包装类装箱拆箱：自动/非自动装箱拆箱写法")
    void test包装类装箱拆箱() {
        String output = captureOutput(包装类装箱拆箱::demo);
        assertThat(output).isEqualTo("i1 = [10]\n"
            + "i2 = [10]\n"
            + "i3 = [10]\n"
            + "i4 = [10]\n"
            + "i5 = [10]\n"
            + "i1 == i2 is [true]\n"
            + "i1 == i4 is [true]\n");
    }

    @Test
    @DisplayName("数值溢出：long 最大值 +1 溢出为最小值")
    void test数值溢出() {
        String output = captureOutput(数值溢出::demo);
        assertThat(output).contains("-9223372036854775808\ntrue\n");
        assertThat(output).contains("9223372036854775808");
    }

    @Test
    @DisplayName("枚举判等：枚举常量字段为同一实例，== 判等为 true")
    void test枚举判等() {
        String output = captureOutput(枚举判等::demo);
        assertThat(output).isEqualTo("true\n");
    }

    @Test
    @DisplayName("浮点数舍入：double/float 舍入结果不一致，BigDecimal 舍入正确")
    void test浮点数舍入() {
        String output = captureOutput(浮点数舍入::demo);
        assertThat(output).isEqualTo("====================== wrong1 ======================\n"
            + "3.4\n"
            + "3.3\n"
            + "====================== wrong2 ======================\n"
            + "3.35\n"
            + "3.34\n"
            + "====================== right ======================\n"
            + "3.3\n"
            + "3.4\n");
    }

    @Test
    @DisplayName("数值计算示例：浮点数精度丢失与 BigDecimal 正确用法")
    void test数值计算示例() {
        String output = captureOutput(数值计算示例::demo);
        assertThat(output).contains("0.30000000000000004");
        assertThat(output).contains("401.49999999999994");
        // right() 部分：BigDecimal 字符串构造方法计算结果精确
        assertThat(output).contains("0.3\n0.2\n401.500\n1.233\n");
    }

    @Test
    @DisplayName("equals和CompareTo：indexOf 与 binarySearch 结果不一致的坑")
    void testEquals和CompareTo() {
        assertThatCode(equals和CompareTo::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Integer判等：Integer 缓存池对 == 判等的影响")
    void testInteger判等() {
        assertThatCode(Integer判等::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Lombok生成Equals的问题：字段排除与继承 callSuper 配置")
    void testLombok生成Equals的问题() {
        assertThatCode(Lombok生成Equals的问题::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("String判等：== 与 equals 判等差异")
    void testString判等() {
        assertThatCode(String判等::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("自定义equals：错误写法与正确写法")
    void test自定义equals() {
        assertThatCode(自定义equals::demo).doesNotThrowAnyException();
    }

}
