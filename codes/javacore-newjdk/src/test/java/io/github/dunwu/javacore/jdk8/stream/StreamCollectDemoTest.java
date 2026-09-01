package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamCollectDemo} 单元测试。
 */
@DisplayName("Java 8 Stream collect 收集器示例测试")
public class StreamCollectDemoTest {

    @Test
    @DisplayName("示例 1：toList 与 toSet 归集为集合")
    public void testToCollection() {
        String output = captureOutput(StreamCollectDemo::toCollection);
        assertThat(output)
            .contains("toList: [Java, Go, Kotlin, Java, C]")
            .contains("toSet 去重: 4 个");
    }

    @Test
    @DisplayName("示例 2：toMap 归集为 Map 并处理 key 冲突")
    public void testToMapDemo() {
        String output = captureOutput(StreamCollectDemo::toMapDemo);
        assertThat(output).contains("toMap 单词长度: {Java=4, Go=2, Kotlin=6, C=1}");
    }

    @Test
    @DisplayName("示例 3：joining 拼接为字符串")
    public void testJoiningDemo() {
        String output = captureOutput(StreamCollectDemo::joiningDemo);
        assertThat(output).contains("joining: [Java, Go, Kotlin, Java, C]");
    }

    @Test
    @DisplayName("示例 4：counting/summing/averaging/maxBy/summarizing 聚合统计")
    public void testStatistics() {
        String output = captureOutput(StreamCollectDemo::statistics);
        assertThat(output)
            .contains("counting: 5")
            .contains("summingInt: 15")
            .contains("averagingDouble: 3.0")
            .contains("maxBy: 5")
            .contains("summarizing: count=5, sum=15, min=1, max=5");
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
