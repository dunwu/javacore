package io.github.dunwu.javacore.jdk8.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link LongAdderDemo} 单元测试。
 */
@DisplayName("Java 8 LongAdder/LongAccumulator 示例测试")
public class LongAdderDemoTest {

    @Test
    @DisplayName("示例 1：LongAdder 基础用法与 sumThenReset")
    public void testBasicUsage() {
        String output = captureOutput(LongAdderDemo::basicUsage);
        assertThat(output)
            .contains("LongAdder sum: 11, sumThenReset: 11")
            .contains("reset 后 sum: 0");
    }

    @Test
    @DisplayName("示例 2：8 线程并发累加 100 万次结果精确")
    public void testConcurrentAccuracy() {
        String output = captureOutput(LongAdderDemo::concurrentAccuracy);
        assertThat(output).contains("8 线程并发累加结果: 8000000（期望 8000000）");
    }

    @Test
    @DisplayName("示例 3：LongAccumulator 自定义累积函数并发求最大值")
    public void testAccumulatorMax() {
        String output = captureOutput(LongAdderDemo::accumulatorMax);
        assertThat(output).contains("LongAccumulator 并发求最大值 >= 1: true");
    }

    @Test
    @DisplayName("示例 4：AtomicLong 并发累加与 LongAdder 结果一致")
    public void testCompareWithAtomicLong() {
        String output = captureOutput(LongAdderDemo::compareWithAtomicLong);
        assertThat(output).contains("AtomicLong 并发累加结果: 8000000");
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
