package io.github.dunwu.javacore.jdk21.thread;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link VirtualThreadDemo} 单元测试。
 */
@DisplayName("Java 21 虚拟线程示例测试")
public class VirtualThreadDemoTest {

    @Test
    @DisplayName("示例 1：Thread.ofVirtual() 构建器创建虚拟线程")
    public void testOfVirtualBuilder() {
        String output = captureOutput(VirtualThreadDemo::ofVirtualBuilder);
        assertThat(output).contains("my-virtual-thread 是虚拟线程吗: true");
    }

    @Test
    @DisplayName("示例 2：Thread.startVirtualThread() 快捷方式创建虚拟线程")
    public void testStartVirtualThreadShortcut() {
        String output = captureOutput(VirtualThreadDemo::startVirtualThreadShortcut);
        assertThat(output).contains("startVirtualThread 创建: true");
    }

    @Test
    @DisplayName("示例 3：newVirtualThreadPerTaskExecutor 让千个阻塞任务近乎并发完成")
    public void testVirtualThreadPerTaskExecutor() {
        String output = captureOutput(VirtualThreadDemo::virtualThreadPerTaskExecutor);
        assertThat(output)
            .contains("1000 个各睡眠 100ms 的任务总耗时: ")
            .contains("远小于串行所需的 100000 ms，说明任务近乎完全并发");
    }

    @Test
    @DisplayName("示例 4：平台线程 isVirtual 为 false")
    public void testPlatformThreadComparison() {
        String output = captureOutput(VirtualThreadDemo::platformThreadComparison);
        assertThat(output).contains("平台线程 isVirtual: false");
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
