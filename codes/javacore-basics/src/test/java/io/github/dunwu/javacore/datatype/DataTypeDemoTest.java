package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * datatype 包数据类型示例单元测试
 * <p>
 * 注：StringInternBenchmarkDemo 为性能基准（1000 万次 intern），不在单元测试中运行
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
    void testBigDecimalEqualityDemo() {
        String output = captureOutput(BigDecimalEqualityDemo::demo);
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
    void testStringConcatDemo() {
        String output = captureOutput(StringConcatDemo::demo);
        assertThat(output).isEqualTo("str = abc\n");
    }

    @Test
    @DisplayName("值类型使用示例：反射依次执行 demo1~demo10")
    void testPrimitiveTypeDemo() {
        String output = captureOutput(PrimitiveTypeDemo::demo);
        assertThat(output).contains("整型的最大值：2147483647");
        assertThat(output).contains("整型的最大值 + 1：-2147483648");
        assertThat(output).contains("ch1 = a");
        assertThat(output).contains("两个小数相乘：9.0");
        assertThat(output).contains("1 + 2 = 12");
        assertThat(output).contains("1 + 2 = 3");
    }

    @Test
    @DisplayName("包装类型使用示例：打印各包装类的位数与取值范围")
    void testWrapperTypeDemo() {
        String output = captureOutput(WrapperTypeDemo::demo);
        assertThat(output).contains("基本类型：byte 二进制位数：8");
        assertThat(output).contains("最小值：Integer.MIN_VALUE=-2147483648");
        assertThat(output).contains("最大值：Long.MAX_VALUE=9223372036854775807");
        assertThat(output).contains("包装类：java.lang.Double");
    }

    @Test
    @DisplayName("包装类装箱拆箱：自动/手动装箱拆箱写法，以及 == 与 equals 在缓存池内外的差异")
    void testAutoboxingDemo() {
        String output = captureOutput(AutoboxingDemo::demo);
        assertThat(output).isEqualTo("i1 = [10], i2 = [10]\n"
            + "i3 = [128], i4 = [128]\n"
            + "i5 = [10], i6 = [10]\n"
            // 10 在缓存范围内，== 与 equals 都为 true
            + "i1 == i2 is [true]\n"
            + "i1.equals(i2) is [true]\n"
            // 128 超出缓存范围，== 为 false，equals 为 true
            + "i3 == i4 is [false]\n"
            + "i3.equals(i4) is [true]\n"
            // 与基本类型比较时自动拆箱，按数值比较
            + "i3 == 128 is [true]\n");
    }

    @Test
    @DisplayName("数值溢出：long 最大值 +1 溢出为最小值")
    void testNumericOverflowDemo() {
        String output = captureOutput(NumericOverflowDemo::demo);
        assertThat(output).contains("-9223372036854775808\ntrue\n");
        assertThat(output).contains("9223372036854775808");
    }

    @Test
    @DisplayName("枚举判等：枚举常量字段为同一实例，== 判等为 true")
    void testEnumEqualityDemo() {
        String output = captureOutput(EnumEqualityDemo::demo);
        assertThat(output).isEqualTo("true\n");
    }

    @Test
    @DisplayName("浮点数舍入：double/float 舍入结果不一致，BigDecimal 舍入正确")
    void testFloatRoundingDemo() {
        String output = captureOutput(FloatRoundingDemo::demo);
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
    void testNumericCalculationDemo() {
        String output = captureOutput(NumericCalculationDemo::demo);
        assertThat(output).isEqualTo("====================== wrong1 ======================\n"
            // double 运算存在精度丢失
            + "0.30000000000000004\n"
            + "0.19999999999999996\n"
            + "401.49999999999994\n"
            + "1.2329999999999999\n"
            + "1.0499999999999998\n"
            + "====================== wrong2 ======================\n"
            // BigDecimal 用 double 构造方法初始化，把 double 的误差原样带进来并完整暴露
            + "0.3000000000000000166533453693773481063544750213623046875\n"
            + "0.1999999999999999555910790149937383830547332763671875\n"
            + "401.49999999999996802557689079549163579940795898437500\n"
            + "1.232999999999999971578290569595992565155029296875\n"
            + "====================== right ======================\n"
            // BigDecimal 用字符串构造方法初始化，计算结果精确
            + "0.3\n"
            + "0.2\n"
            + "401.500\n"
            + "1.233\n"
            + "====================== testScale ======================\n"
            // scale 取决于初始化写法："100" 为 0，String.valueOf(100d) 与 valueOf(100d) 为 1
            + "scale 0 precision 3 result 401.500\n"
            + "scale 1 precision 4 result 401.5000\n"
            + "scale 0 precision 3 result 401.500\n"
            + "scale 1 precision 4 result 401.5000\n"
            + "scale 1 precision 4 result 401.5000\n");
    }

    @Test
    @DisplayName("equals和CompareTo：indexOf 与 binarySearch 结果不一致的坑")
    void testEqualsCompareToDemo() {
        String output = captureOutput(EqualsCompareToDemo::demo);
        assertThat(output).isEqualTo("ArrayList.indexOf\n"
            // indexOf 用 equals 比较，Student 未重写 equals，找不到 id 相同的元素
            + "Collections.binarySearch\n"
            // binarySearch 用 compareTo 比较，只比 id 不比 name，于是"找到"了一个名字不同的元素。
            // 这行由 compareTo 内部打印，出现的位置取决于排序与二分查找的比较次数
            + "this EqualsCompareToDemo.Student(id=2, name=wang) == other EqualsCompareToDemo.Student(id=2, name=li)\n"
            + "index1 = -1\n"
            + "index2 = 0\n"
            // right()：equals 与 compareTo 保持一致，两者都找不到 name 为 li 的元素
            + "ArrayList.indexOf\n"
            + "Collections.binarySearch\n"
            + "index1 = -1\n"
            + "index2 = -1\n");
    }

    @Test
    @DisplayName("Integer判等：Integer 缓存池对 == 判等的影响")
    void testIntegerEqualityDemo() {
        String output = captureOutput(IntegerEqualityDemo::demo);
        assertThat(output).isEqualTo("\n"
            // 127 在缓存池 [-128, 127] 内，valueOf 返回同一实例
            + "Integer a = 127;\n"
            + "Integer b = 127;\n"
            + "a == b ? true\n"
            + "\n"
            // 128 超出缓存池，valueOf 返回不同实例
            + "Integer c = 128;\n"
            + "Integer d = 128;\n"
            + "c == d ? false\n"
            + "\n"
            // new Integer 总是新建实例，与缓存池中的实例不同
            + "Integer e = 127;\n"
            + "Integer f = new Integer(127);\n"
            + "e == f ? false\n"
            + "\n"
            + "Integer g = new Integer(127);\n"
            + "Integer h = new Integer(127);\n"
            + "g == h ? false\n"
            + "\n"
            // 与基本类型比较时自动拆箱，按数值比较
            + "Integer i = 128;\n"
            + "int j = 128;\n"
            + "i == j ? true\n");
    }

    @Test
    @DisplayName("Lombok生成Equals的问题：字段排除与继承 callSuper 配置")
    void testLombokEqualsPitfallDemo() {
        String output = captureOutput(LombokEqualsPitfallDemo::demo);
        assertThat(output).isEqualTo(
            // Person 用 @EqualsAndHashCode.Exclude 排除了 name，只比 identity，故相等
            "person1.equals(person2) ? true\n"
            // Employee 配置了 callSuper = true，会调用 Person.equals；但 identity 不同，故不相等
            + "employee1.equals(employee2) ? false\n");
    }

    @Test
    @DisplayName("String判等：== 与 equals 判等差异")
    void testStringEqualityDemo() {
        String output = captureOutput(StringEqualityDemo::demo);
        assertThat(output).isEqualTo("\n"
            // 字面量走字符串常量池，是同一实例
            + "String a = \"1\";\n"
            + "String b = \"1\";\n"
            + "a == b ? true\n"
            + "\n"
            // new String 总是新建实例
            + "String c = new String(\"2\");\n"
            + "String d = new String(\"2\");\n"
            + "c == d ? false\n"
            + "\n"
            // intern() 返回常量池中的同一实例
            + "String e = new String(\"3\").intern();\n"
            + "String f = new String(\"3\").intern();\n"
            + "e == f ? true\n"
            + "\n"
            // 这里比较的是 equals 而非 ==，两个新建实例内容相同
            + "String g = new String(\"4\");\n"
            + "String h = new String(\"4\");\n"
            + "g == h ? true\n");
    }

    @Test
    @DisplayName("自定义equals：错误写法与正确写法")
    void testCustomEqualsDemo() {
        String output = captureOutput(CustomEqualsDemo::demo);
        String[] lines = output.split("\n");
        assertThat(lines).hasSize(10);
        // wrong()：未重写 equals，沿用 Object 的引用比较，字段相同的实例仍不相等
        assertThat(lines[0]).isEqualTo("p1.equals(p2) ? false");
        assertThat(lines[1]).isEqualTo("p1.equals(p3) ? false");
        // wrong2()：未判空、未判类型，直接强转导致抛异常。
        // 异常的 message 文本随 JDK 版本变化（JDK 14+ 的 helpful NPE 会指出具体字段名，
        // ClassCastException 会带上 module 与 ClassLoader 信息），故只断言异常类型。
        assertThat(lines[2]).startsWith("java.lang.NullPointerException");
        assertThat(lines[3]).startsWith("java.lang.ClassCastException");
        assertThat(lines[4]).isEqualTo("p1.equals(p2) ? true");
        // 未重写 hashCode，字段相等的实例落在 HashSet 的不同桶中
        assertThat(lines[5]).isEqualTo("points.contains(p2) ? false");
        // right()：判空、判类型、重写 hashCode
        assertThat(lines[6]).isEqualTo("p1.equals(null) ? false");
        assertThat(lines[7]).isEqualTo("p1.equals(expression) ? false");
        assertThat(lines[8]).isEqualTo("p1.equals(p2) ? true");
        assertThat(lines[9]).isEqualTo("points.contains(p2) ? true");
    }

}
