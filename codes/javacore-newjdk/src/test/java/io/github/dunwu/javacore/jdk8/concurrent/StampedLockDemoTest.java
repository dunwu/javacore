package io.github.dunwu.javacore.jdk8.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StampedLockDemo} 单元测试。
 */
@DisplayName("Java 8 StampedLock 邮戳锁示例测试")
public class StampedLockDemoTest {

    @Test
    @DisplayName("多线程并发读写验证：乐观读不读脏数据，最终坐标一致")
    public void testConcurrentReadWrite() {
        String output = captureOutput(StampedLockDemo::concurrentReadWrite);
        assertThat(output)
            .contains("并发读写完成，最终到原点距离: 565685")
            .contains("与期望值一致: true");
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
