package io.github.dunwu.javacore.jdk9.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CompletableFutureEnhanceDemo} 单元测试。
 */
@DisplayName("Java 9 CompletableFuture 增强示例测试")
public class CompletableFutureEnhanceDemoTest {

    @Test
    @DisplayName("示例 1：orTimeout 超时后以 TimeoutException 异常完成")
    public void testOrTimeoutDemo() {
        String output = captureOutput(CompletableFutureEnhanceDemo::orTimeoutDemo);
        assertThat(output).contains("orTimeout 超时，异常类型: TimeoutException");
    }

    @Test
    @DisplayName("示例 2：completeOnTimeout 超时后以默认值正常完成")
    public void testCompleteOnTimeoutDemo() {
        String output = captureOutput(CompletableFutureEnhanceDemo::completeOnTimeoutDemo);
        assertThat(output).contains("completeOnTimeout 结果: 默认结果");
    }

    @Test
    @DisplayName("示例 3：任务及时完成时 completeOnTimeout 不生效")
    public void testNoTimeoutScenario() {
        String output = captureOutput(CompletableFutureEnhanceDemo::noTimeoutScenario);
        assertThat(output).contains("及时完成场景结果: 及时完成");
    }

    @Test
    @DisplayName("示例 4：copy 副本随原 Future 完成而完成")
    public void testCopyDemo() {
        String output = captureOutput(CompletableFutureEnhanceDemo::copyDemo);
        assertThat(output).contains("copy 副本结果: 原始任务完成");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

}
