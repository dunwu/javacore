package io.github.dunwu.javacore.jdk8.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ArraysCollectionDemo} 单元测试。
 */
@DisplayName("Java 8 Arrays 与集合批量操作增强示例测试")
public class ArraysCollectionDemoTest {

    @Test
    @DisplayName("示例 1：parallelSort 并行排序、setAll 按索引填充、stream 求和")
    public void testArraysEnhance() {
        String output = captureOutput(ArraysCollectionDemo::arraysEnhance);
        assertThat(output)
            .contains("parallelSort: [1, 2, 3, 5, 8, 9]")
            .contains("setAll 平方数: [0, 1, 4, 9, 16]")
            .contains("Arrays.stream 求和: 28");
    }

    @Test
    @DisplayName("示例 2：Spliterator trySplit 对半拆分后各半区独立消费")
    public void testSpliteratorDemo() {
        String output = captureOutput(ArraysCollectionDemo::spliteratorDemo);
        assertThat(output).contains("spliterator 拆分: [ab] 和 [cd]");
    }

    @Test
    @DisplayName("示例 3：forEach/removeIf/sort 集合批量操作与 stream 过滤")
    public void testCollectionBatchOps() {
        String output = captureOutput(ArraysCollectionDemo::collectionBatchOps);
        assertThat(output)
            .contains("forEach: Java Go Kotlin C")
            .contains("removeIf 短语言: [Java, Kotlin]")
            .contains("sort 按长度倒序: [Kotlin, Java]")
            .contains("stream filter: [Java]");
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
