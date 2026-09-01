package io.github.dunwu.javacore.jdk11.optional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OptionalEnhanceDemo} 单元测试。
 */
@DisplayName("Java 11 Optional 与 Predicate 增强示例测试")
public class OptionalEnhanceDemoTest {

    @Test
    @DisplayName("示例 1：isEmpty 判断空 Optional 并配合方法引用统计空值个数")
    public void testIsEmptyDemo() {
        String output = captureOutput(OptionalEnhanceDemo::isEmptyDemo);
        assertThat(output)
            .contains("Optional.empty().isEmpty(): true")
            .contains("Optional.of(\"a\").isEmpty(): false")
            .contains("空 Optional 个数: 1");
    }

    @Test
    @DisplayName("示例 2：Predicate.not 取反谓词与传统写法结果一致")
    public void testPredicateNotDemo() {
        String output = captureOutput(OptionalEnhanceDemo::predicateNotDemo);
        assertThat(output)
            .contains("非空白元素: [Java, Kotlin]")
            .contains("传统写法结果一致: true");
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
