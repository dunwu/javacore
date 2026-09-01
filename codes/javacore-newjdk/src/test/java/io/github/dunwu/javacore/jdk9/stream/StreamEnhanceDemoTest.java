package io.github.dunwu.javacore.jdk9.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamEnhanceDemo} 单元测试。
 */
@DisplayName("Java 9 Stream 增强示例测试")
public class StreamEnhanceDemoTest {

    @Test
    @DisplayName("示例 1：takeWhile 截取前缀与 dropWhile 丢弃前缀")
    public void testTakeWhileAndDropWhile() {
        String output = captureOutput(StreamEnhanceDemo::takeWhileAndDropWhile);
        assertThat(output)
            .contains("takeWhile 奇数前缀: [1, 3, 5]")
            .contains("dropWhile 丢弃奇数前缀: [2, 4, 6]");
    }

    @Test
    @DisplayName("示例 2：ofNullable 将 null 转为空流、非 null 转为单元素流")
    public void testOfNullableDemo() {
        String output = captureOutput(StreamEnhanceDemo::ofNullableDemo);
        assertThat(output)
            .contains("ofNullable(null) 元素个数: 0")
            .contains("ofNullable(\"Java\") 元素个数: 1");
    }

    @Test
    @DisplayName("示例 3：带终止条件的 iterate 生成有限序列")
    public void testIterateWithPredicate() {
        String output = captureOutput(StreamEnhanceDemo::iterateWithPredicate);
        assertThat(output).contains("iterate 带终止条件: [1, 2, 4, 8, 16, 32, 64]");
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
