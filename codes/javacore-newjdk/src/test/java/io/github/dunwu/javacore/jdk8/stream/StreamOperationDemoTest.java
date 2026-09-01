package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamOperationDemo} 单元测试。
 */
@DisplayName("Java 8 Stream 中间操作示例测试")
public class StreamOperationDemoTest {

    @Test
    @DisplayName("示例 1：filter 按条件过滤与 map 一对一转换")
    public void testFilterAndMap() {
        String output = captureOutput(StreamOperationDemo::filterAndMap);
        assertThat(output)
            .contains("filter 偶数: [2, 4, 6]")
            .contains("map 求长度: [4, 2, 6]");
    }

    @Test
    @DisplayName("示例 2：flatMap 一对多转换并拍平")
    public void testFlatMapDemo() {
        String output = captureOutput(StreamOperationDemo::flatMapDemo);
        assertThat(output).contains("flatMap 拍平: [H, i, J, a, v, a]");
    }

    @Test
    @DisplayName("示例 3：distinct 去重与 sorted 自定义排序")
    public void testDistinctAndSorted() {
        String output = captureOutput(StreamOperationDemo::distinctAndSorted);
        assertThat(output)
            .contains("distinct 去重: [1, 2, 3]")
            .contains("sorted 按长度: [apple, banana, cherry]");
    }

    @Test
    @DisplayName("示例 4：limit/skip 截取、peek 旁路观察与惰性求值")
    public void testLimitSkipPeek() {
        String output = captureOutput(StreamOperationDemo::limitSkipPeek);
        assertThat(output)
            .contains("skip(2).limit(3): [3, 4, 5]")
            .contains("peek 观察: 1")
            .contains("peek 后结果: [10, 20, 30]")
            .contains("惰性求值：中间操作未触发")
            .doesNotContain("这行不会被打印");
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
