package io.github.dunwu.javacore.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 异常示例测试：验证各异常示例的输出与行为。
 * 反例（运行即抛异常）不在此测试：MyExceptionDemo、RuntimeExceptionDemo。
 */
@DisplayName("异常示例测试")
public class ExceptionDemoTest {

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
    @DisplayName("AssertDemo：断言失败时抛出 AssertionError（测试环境开启 -ea）")
    public void testAssertDemo() {
        assertThatThrownBy(AssertDemo::demo)
            .isInstanceOf(AssertionError.class)
            .hasMessage("数组长度不为0");
    }

    @Test
    @DisplayName("ExceptionChainDemo：异常链")
    public void testExceptionChainDemo() {
        assertThatThrownBy(ExceptionChainDemo::demo)
            .isInstanceOf(ExceptionChainDemo.MyException2.class)
            .hasMessage("出现 MyException2")
            .hasCauseInstanceOf(ExceptionChainDemo.MyException1.class);
    }

    @Test
    @DisplayName("ExceptionOverrideDemo：重写方法不能抛出更宽的受检异常")
    public void testExceptionOverrideDemo() {
        assertThatCode(ExceptionOverrideDemo::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("FinallyOverrideExceptionDemo：finally 中的异常覆盖原始异常")
    public void testFinallyOverrideExceptionDemo() {
        String output = captureOutput(FinallyOverrideExceptionDemo::demo);
        assertThat(output).isEqualTo("C\n");
    }

    @Test
    @DisplayName("ThrowDemo：方法内主动抛出异常")
    public void testThrowDemo() {
        String output = captureOutput(ThrowDemo::demo);
        assertThat(output).isEqualTo("java.lang.RuntimeException: 抛出一个异常\n");
    }

    @Test
    @DisplayName("ThrowsDemo：声明抛出受检异常")
    public void testThrowsDemo() {
        String output = captureOutput(ThrowsDemo::demo);
        assertThat(output).contains("反射获取 digits 方法成功");
    }

    @Test
    @DisplayName("TryCatchDemo：捕获异常")
    public void testTryCatchDemo() {
        String output = captureOutput(TryCatchDemo::demo);
        assertThat(output).isEqualTo("出现异常了：java.lang.ArithmeticException: / by zero\n");
    }

    @Test
    @DisplayName("TryCatchFinallyDemo：finally 总是执行")
    public void testTryCatchFinallyDemo() {
        String output = captureOutput(TryCatchFinallyDemo::demo);
        assertThat(output).isEqualTo("出现异常了：java.lang.ArithmeticException: / by zero\n不管是否出现异常，都执行此代码\n");
    }

}
