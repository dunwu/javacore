package io.github.dunwu.javacore.jdk8.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CompletableFutureDemo} 单元测试。
 */
@DisplayName("Java 8 CompletableFuture 异步编程示例测试")
public class CompletableFutureDemoTest {

    @Test
    @DisplayName("示例 1：supplyAsync 开启异步任务并获取结果")
    public void testAsyncCreation() {
        String output = captureOutput(CompletableFutureDemo::asyncCreation);
        assertThat(output).contains("supplyAsync 结果: Java");
    }

    @Test
    @DisplayName("示例 2：thenApply 链式转换与 thenAccept 消费结果")
    public void testSerialComposition() {
        String output = captureOutput(CompletableFutureDemo::serialComposition);
        assertThat(output)
            .contains("thenApply 链式转换: JAVA 8")
            .contains("thenAccept 消费: hello");
    }

    @Test
    @DisplayName("示例 3：thenCombine 并行任务合并计算折后价")
    public void testParallelComposition() {
        String output = captureOutput(CompletableFutureDemo::parallelComposition);
        assertThat(output).contains("thenCombine 折后价: 80.0");
    }

    @Test
    @DisplayName("示例 4：allOf 等待全部任务完成")
    public void testWaitAll() {
        String output = captureOutput(CompletableFutureDemo::waitAll);
        assertThat(output).contains("allOf: 全部任务完成");
    }

    @Test
    @DisplayName("示例 5：exceptionally 兜底与 handle 同时处理结果和异常")
    public void testExceptionHandling() {
        String output = captureOutput(CompletableFutureDemo::exceptionHandling);
        assertThat(output)
            .contains("异常兜底: java.lang.RuntimeException: 模拟失败")
            .contains("handle 捕获异常");
    }

    @Test
    @DisplayName("示例 6：自定义线程池执行异步任务")
    public void testCustomExecutor() {
        String output = captureOutput(CompletableFutureDemo::customExecutor);
        assertThat(output).contains("自定义线程池: true");
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

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;

    }

}
