package io.github.dunwu.javacore.operator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 操作符示例测试
 */
public class OperatorDemoTest {

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
    @DisplayName("赋值运算符：=、+=、-=、*= 等")
    void testAssignmentOperatorDemo() {
        String output = captureOutput(AssignmentOperatorDemo::demo);
        assertThat(output)
            .contains("c = a + b = 30")
            .contains("c += a  = 40")
            .contains("c -= a = 30")
            .contains("c *= a = 300");
    }

    @Test
    @DisplayName("位运算符：&、|、^、~、移位运算")
    void testBitsOperatorDemo() {
        String output = captureOutput(BitsOperatorDemo::demo);
        assertThat(output)
            .contains("a & b = 12")
            .contains("a | b = 61")
            .contains("a ^ b = 49")
            .contains("~a = -61")
            .contains("a << 2 = 240")
            .contains("a >> 2  = 15")
            .contains("a >>> 2 = 15");
    }

    @Test
    @DisplayName("条件（三元）运算符：? :")
    void testConditionalOperatorDemo() {
        String output = captureOutput(ConditionalOperatorDemo::demo);
        assertThat(output).isEqualTo("最大值为：10\n");
    }

    @Test
    @DisplayName("instanceof 运算符：判断对象类型")
    void testInstanceofOperatorDemo() {
        String output = captureOutput(InstanceofOperatorDemo::demo);
        assertThat(output).isEqualTo("true\n");
    }

    @Test
    @DisplayName("逻辑运算符：&&、||、!")
    void testLogicalOperatorDemo() {
        String output = captureOutput(LogicalOperatorDemo::demo);
        assertThat(output)
            .isEqualTo("a && b = false\na || b = true\n!(a && b) = true\n");
    }

    @Test
    @DisplayName("算术运算符：+、-、*、/、%、++")
    void testMathOperatorDemo() {
        String output = captureOutput(MathOperatorDemo::demo);
        assertThat(output)
            .contains("x + y = 30")
            .contains("x % y = 0")
            .contains("x++ = 20")
            .contains("++x = 21");
    }

    @Test
    @DisplayName("关系运算符：==、!=、>、<")
    void testRelationOperatorDemo() {
        String output = captureOutput(RelationOperatorDemo::demo);
        assertThat(output)
            .contains("x == y = false")
            .contains("x != y = true")
            .contains("x > y = true");
    }

}
