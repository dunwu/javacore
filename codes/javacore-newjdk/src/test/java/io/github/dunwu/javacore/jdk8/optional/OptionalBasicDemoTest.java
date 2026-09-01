package io.github.dunwu.javacore.jdk8.optional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OptionalBasicDemo} 单元测试。
 */
@DisplayName("Java 8 Optional 基础用法示例测试")
public class OptionalBasicDemoTest {

    @Test
    @DisplayName("示例 1：of/ofNullable/empty 三种创建方式")
    public void testCreateOptional() {
        String output = captureOutput(OptionalBasicDemo::createOptional);
        assertThat(output)
            .contains("of: Java")
            .contains("ofNullable(null) isPresent: false")
            .contains("empty 无值: true");
    }

    @Test
    @DisplayName("示例 2：orElse 始终计算参数，orElseGet 有值时不执行 supplier")
    public void testOrElseVsOrElseGet() {
        String output = captureOutput(OptionalBasicDemo::orElseVsOrElseGet);
        assertThat(output)
            .contains("orElse: 默认值")
            .contains("orElseGet: 惰性计算的默认值")
            .contains("[computeDefault 被调用]")
            .contains("orElse 即使有值也计算参数: Java")
            .contains("orElseGet 有值时不计算: Java");
        // computeDefault 只被 orElse 触发一次
        assertThat(output).containsOnlyOnce("[computeDefault 被调用]");
    }

    @Test
    @DisplayName("示例 3：orElseThrow 值缺失时抛出自定义异常")
    public void testOrElseThrowDemo() {
        String output = captureOutput(OptionalBasicDemo::orElseThrowDemo);
        assertThat(output).contains("orElseThrow: 自定义：值不存在");
    }

    @Test
    @DisplayName("示例 4：ifPresent 执行动作与 Optional.of(null) 抛 NPE")
    public void testIfPresentAndOfNull() {
        String output = captureOutput(OptionalBasicDemo::ifPresentAndOfNull);
        assertThat(output)
            .contains("ifPresent: Java")
            .contains("Optional.of(null) 抛出 NullPointerException");
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
