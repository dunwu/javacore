package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamGroupingDemo} 单元测试。
 */
@DisplayName("Java 8 Stream 分组与分区示例测试")
public class StreamGroupingDemoTest {

    @Test
    @DisplayName("示例 1：groupingBy 单级分组按班级归集学生")
    public void testGroupByClazz() {
        String output = captureOutput(StreamGroupingDemo::groupByClazz);
        assertThat(output)
            .contains("一班: 张三, 李四, 孙七")
            .contains("二班: 王五, 赵六");
    }

    @Test
    @DisplayName("示例 2：下游收集器统计各班人数与平均分")
    public void testDownstreamCollector() {
        String output = captureOutput(StreamGroupingDemo::downstreamCollector);
        assertThat(output)
            .contains("各班人数: {一班=3, 二班=2}")
            .contains("各班平均分: {一班=75.0, 二班=77.5}");
    }

    @Test
    @DisplayName("示例 3：groupingBy 嵌套 partitioningBy 多级分组")
    public void testNestedGrouping() {
        String output = captureOutput(StreamGroupingDemo::nestedGrouping);
        assertThat(output)
            .contains("一班及格: [张三, 李四]")
            .contains("一班不及格: [孙七]");
    }

    @Test
    @DisplayName("示例 4：partitioningBy 二分分区且两个 key 一定存在")
    public void testPartitioning() {
        String output = captureOutput(StreamGroupingDemo::partitioning);
        assertThat(output).contains("及格人数: 4, 不及格人数: 1");
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
