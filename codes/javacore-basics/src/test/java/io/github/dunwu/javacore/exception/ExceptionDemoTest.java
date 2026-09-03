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

    @Test
    @DisplayName("TryWithResourcesDemo：正常与异常两条路径都会自动关闭资源")
    public void testTryWithResourcesAutoClose() {
        String output = captureOutput(TryWithResourcesDemo::autoClose);
        assertThat(output).isEqualTo("创建资源 A\n"
            + "使用资源 A\n"
            + "关闭资源 A\n"
            + "创建资源 B\n"
            + "使用资源 B\n"
            + "关闭资源 B\n"
            + "捕获到: B 使用中途出错\n");
    }

    @Test
    @DisplayName("TryWithResourcesDemo：声明多个资源时，关闭顺序与声明顺序相反")
    public void testTryWithResourcesCloseOrder() {
        String output = captureOutput(TryWithResourcesDemo::closeOrder);
        assertThat(output).isEqualTo("创建资源 R1\n"
            + "创建资源 R2\n"
            + "使用资源 R1\n"
            + "使用资源 R2\n"
            + "关闭资源 R2\n"
            + "关闭资源 R1\n");
    }

    @Test
    @DisplayName("TryWithResourcesDemo：close() 的异常被抑制，主异常得以保留")
    public void testTryWithResourcesSuppressed() {
        String output = captureOutput(TryWithResourcesDemo::suppressedException);
        assertThat(output).isEqualTo("创建资源 S\n"
            + "使用资源 S\n"
            + "关闭资源 S\n"
            + "主异常: 业务异常\n"
            + "被抑制的异常: S 关闭失败\n");
    }

    @Test
    @DisplayName("TryWithResourcesDemo：对照手写 finally，业务异常被 close() 异常彻底覆盖")
    public void testTryWithResourcesCompareWithFinally() {
        String output = captureOutput(TryWithResourcesDemo::compareWithFinally);
        // 与 testTryWithResourcesSuppressed 对照：同样是两个异常，这里业务异常毫无痕迹，suppressed 个数为 0
        assertThat(output).isEqualTo("创建资源 F\n"
            + "使用资源 F\n"
            + "关闭资源 F\n"
            + "最终抛出的异常: F 关闭失败\n"
            + "被抑制的异常个数: 0\n");
    }

}
