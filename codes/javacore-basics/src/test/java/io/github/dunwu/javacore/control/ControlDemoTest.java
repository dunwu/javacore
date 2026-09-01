package io.github.dunwu.javacore.control;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 控制语句示例测试
 */
public class ControlDemoTest {

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("break 语句：提前跳出循环")
    void testBreakDemo() {
        String output = captureOutput(BreakDemo::demo);
        assertThat(output).isEqualTo("0\n1\n2\n示例结束\n");
    }

    @Test
    @DisplayName("continue 语句：跳过本次循环")
    void testContinueDemo() {
        String output = captureOutput(ContinueDemo::demo);
        assertThat(output).contains("i = 2").doesNotContain("i = 3").contains("i = 4");
    }

    @Test
    @DisplayName("do-while 循环：1 到 10 累加")
    void testDoWhileDemo() {
        String output = captureOutput(DoWhileDemo::demo);
        assertThat(output).isEqualTo("1 --> 10 累加的结果为：55\n");
    }

    @Test
    @DisplayName("for 循环：1 到 10 累加")
    void testForDemo() {
        String output = captureOutput(ForDemo::demo);
        assertThat(output).isEqualTo("1 --> 10 累加的结果为：55\n");
    }

    @Test
    @DisplayName("foreach 循环：遍历数组元素")
    void testForeachDemo() {
        String output = captureOutput(ForeachDemo::demo);
        assertThat(output).contains("10,20,30,40,50,").contains("James,Larry,Tom,Lacy,");
    }

    @Test
    @DisplayName("嵌套 for 循环：输出九九乘法表")
    void testForNestedDemo() {
        String output = captureOutput(ForNestedDemo::demo);
        assertThat(output).contains("1*1=1").contains("9*9=81");
    }

    @Test
    @DisplayName("if 语句：单分支条件判断")
    void testIfDemo() {
        String output = captureOutput(IfDemo::demo);
        assertThat(output).isEqualTo("x比y小！\n");
    }

    @Test
    @DisplayName("if-else 语句：判断奇偶数")
    void testIfElseDemo() {
        String output = captureOutput(IfElseDemo::demo);
        assertThat(output).isEqualTo("x是奇数！\n");
    }

    @Test
    @DisplayName("if-else if-else 语句：多分支条件判断")
    void testIfElseifElseDemo() {
        String output = captureOutput(IfElseifElseDemo::demo);
        assertThat(output).isEqualTo("x的值不是1、2、3中的一个！\n");
    }

    @Test
    @DisplayName("嵌套 if 语句：多层条件判断")
    void testIfNestedDemo() {
        String output = captureOutput(IfNestedDemo::demo);
        assertThat(output).isEqualTo("X = 30 and Y = 10\n");
    }

    @Test
    @DisplayName("return 语句：提前结束方法执行")
    void testReturnDemo() {
        String output = captureOutput(ReturnDemo::demo);
        assertThat(output).isEqualTo("0\n1\n2\n").doesNotContain("示例结束");
    }

    @Test
    @DisplayName("switch 语句：基本分支选择")
    void testSwitchDemo01() {
        String output = captureOutput(SwitchDemo01::demo);
        assertThat(output).isEqualTo("x + y = 9\n");
    }

    @Test
    @DisplayName("switch 语句：分数范围判断")
    void testSwitchDemo02() {
        String output = captureOutput(SwitchDemo02::demo);
        assertThat(output).isEqualTo("分数范围：>= 60\n");
    }

    @Test
    @DisplayName("switch 语句：default 默认分支")
    void testSwitchDemo03() {
        String output = captureOutput(SwitchDemo03::demo);
        assertThat(output).isEqualTo("无效选项\n");
    }

    @Test
    @DisplayName("while 循环：1 到 10 累加")
    void testWhileDemo() {
        String output = captureOutput(WhileDemo::demo);
        assertThat(output).isEqualTo("1 --> 10 累加的结果为：55\n");
    }

}
