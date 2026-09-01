package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamReduceDemo} 单元测试。
 */
@DisplayName("Java 8 Stream 终结操作与归约示例测试")
public class StreamReduceDemoTest {

    @Test
    @DisplayName("示例 1：forEach 串行流按遭遇顺序遍历")
    public void testForEachDemo() {
        String output = captureOutput(StreamReduceDemo::forEachDemo);
        assertThat(output).contains("forEach: 1 2 3");
    }

    @Test
    @DisplayName("示例 2：reduce 三种形式（无初始值/带初始值/并行安全版）")
    public void testReduceDemo() {
        String output = captureOutput(StreamReduceDemo::reduceDemo);
        assertThat(output)
            .contains("reduce 求和: 10")
            .contains("reduce 求积: 24")
            .contains("reduce 总长度: 6");
    }

    @Test
    @DisplayName("示例 3：min/max/count 聚合")
    public void testMinMaxCount() {
        String output = captureOutput(StreamReduceDemo::minMaxCount);
        assertThat(output).contains("max: 5, count: 5");
    }

    @Test
    @DisplayName("示例 4：allMatch/anyMatch/noneMatch 短路匹配")
    public void testMatchDemo() {
        String output = captureOutput(StreamReduceDemo::matchDemo);
        assertThat(output)
            .contains("allMatch 偶数: true")
            .contains("anyMatch 大于 7: true")
            .contains("noneMatch 负数: true");
    }

    @Test
    @DisplayName("示例 5：findFirst/findAny 短路查找")
    public void testFindDemo() {
        String output = captureOutput(StreamReduceDemo::findDemo);
        assertThat(output)
            .contains("findFirst: 6")
            .contains("findAny: 6");
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
