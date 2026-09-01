package io.github.dunwu.javacore.jdk9.trywith;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ImprovedTryWithResourcesDemo} 单元测试。
 */
@DisplayName("Java 9 改进的 try-with-resources 示例测试")
public class ImprovedTryWithResourcesDemoTest {

    @Test
    @DisplayName("示例 1：Java 7 写法资源必须在 try 小括号内声明")
    public void testJava7Style() {
        String output = captureOutput(ImprovedTryWithResourcesDemo::java7Style);
        assertThat(output).contains("Java 7 写法");
    }

    @Test
    @DisplayName("示例 2：Java 9 写法可直接复用外部 effectively final 资源")
    public void testJava9Style() {
        String output = captureOutput(ImprovedTryWithResourcesDemo::java9Style);
        assertThat(output)
            .contains("Java 9 写法")
            .contains("reader2 是否已关闭（无法读取已关闭的流，此处仅演示语法）");
    }

    @Test
    @DisplayName("示例 3：可混用新声明资源与外部已声明资源")
    public void testMixedResources() {
        String output = captureOutput(ImprovedTryWithResourcesDemo::mixedResources);
        assertThat(output).contains("外部资源 + 内部资源");
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
