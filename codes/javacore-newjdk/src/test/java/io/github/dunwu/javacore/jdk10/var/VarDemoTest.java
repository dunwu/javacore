package io.github.dunwu.javacore.jdk10.var;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link VarDemo} 单元测试。
 */
@DisplayName("Java 10 局部变量类型推断 var 示例测试")
public class VarDemoTest {

    @Test
    @DisplayName("示例 1：局部变量声明自动推断 String、集合类型")
    public void testLocalVarInference() {
        String output = captureOutput(VarDemo::localVarInference);
        assertThat(output)
            .contains("Hello, Java 10")
            .contains("list 类型: ArrayList")
            .contains("map: {一=1, 二=2}");
    }

    @Test
    @DisplayName("示例 2：增强 for 循环与传统 for 循环中使用 var")
    public void testVarInLoops() {
        String output = captureOutput(VarDemo::varInLoops);
        assertThat(output)
            .contains("一 -> 1")
            .contains("二 -> 2")
            .contains("0 1 2");
    }

    @Test
    @DisplayName("示例 3：try-with-resources 与泛型方法中使用 var")
    public void testVarInTryWithResources() {
        String output = captureOutput(VarDemo::varInTryWithResources);
        assertThat(output)
            .contains("try-with-resources 中的 var")
            .contains("numbers 类型: ArrayList, 内容: [1, 2, 3]");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new AssertionError("被测代码抛出意外异常", e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * 允许抛出受检异常的 Runnable
     */
    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;

    }

}
