package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamCreateDemo} 单元测试。
 */
@DisplayName("Java 8 Stream 创建方式示例测试")
public class StreamCreateDemoTest {

    @Test
    @DisplayName("示例 1：从集合、Stream.of、Arrays.stream 创建流")
    public void testFromCollectionAndOf() {
        String output = captureOutput(StreamCreateDemo::fromCollectionAndOf);
        assertThat(output)
            .contains("集合创建: 3 个元素")
            .contains("Stream.of: 3")
            .contains("Arrays.stream 求和: 15");
    }

    @Test
    @DisplayName("示例 2：iterate 与 generate 无限流配合 limit 截取")
    public void testIterateAndGenerate() {
        String output = captureOutput(StreamCreateDemo::iterateAndGenerate);
        assertThat(output)
            .contains("iterate 生成偶数: [0, 2, 4, 6, 8]")
            .contains("generate 生成随机数（固定种子）: [30, 63, 48]");
    }

    @Test
    @DisplayName("示例 3：基本类型专用流 IntStream 避免装箱开销")
    public void testPrimitiveStream() {
        String output = captureOutput(StreamCreateDemo::primitiveStream);
        assertThat(output)
            .contains("IntStream.range(1, 6) 求和: 15")
            .contains("IntStream.rangeClosed(1, 6) 求和: 21");
    }

    @Test
    @DisplayName("示例 4：字符串转字符流统计字母个数")
    public void testCharsStream() {
        String output = captureOutput(StreamCreateDemo::charsStream);
        assertThat(output).contains("chars 统计字母个数: 4");
    }

    @Test
    @DisplayName("示例 5：空流 Stream.empty")
    public void testEmptyStream() {
        String output = captureOutput(StreamCreateDemo::emptyStream);
        assertThat(output).contains("Stream.empty: 0");
    }

    @Test
    @DisplayName("示例 6：Stream 只能消费一次，二次使用抛 IllegalStateException")
    public void testConsumeOnce() {
        String output = captureOutput(StreamCreateDemo::consumeOnce);
        assertThat(output).contains("Stream 已消费，二次使用抛出 IllegalStateException");
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
