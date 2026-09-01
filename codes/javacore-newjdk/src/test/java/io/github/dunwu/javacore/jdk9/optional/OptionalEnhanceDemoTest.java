package io.github.dunwu.javacore.jdk9.optional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OptionalEnhanceDemo} 单元测试。
 */
@DisplayName("Java 9 Optional 增强示例测试")
public class OptionalEnhanceDemoTest {

    @Test
    @DisplayName("示例 1：ifPresentOrElse 按值是否存在分别执行不同逻辑")
    public void testIfPresentOrElseDemo() {
        String output = captureOutput(OptionalEnhanceDemo::ifPresentOrElseDemo);
        assertThat(output)
            .contains("找到值: Java")
            .contains("值不存在");
    }

    @Test
    @DisplayName("示例 2：or 在值不存在时提供备选 Optional")
    public void testOrDemo() {
        String output = captureOutput(OptionalEnhanceDemo::orDemo);
        assertThat(output).contains("or 备选结果: 默认值");
    }

    @Test
    @DisplayName("示例 3：stream 把 Optional 转流后 flatMap 拼接")
    public void testStreamDemo() {
        String output = captureOutput(OptionalEnhanceDemo::streamDemo);
        assertThat(output).contains("Optional.stream 拼接结果: a, b");
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
