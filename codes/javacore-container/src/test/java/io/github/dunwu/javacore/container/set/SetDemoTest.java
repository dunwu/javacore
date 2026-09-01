package io.github.dunwu.javacore.container.set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * javacore-container set 包示例的单元测试
 */
@DisplayName("Set 示例测试")
public class SetDemoTest {

    @Test
    @DisplayName("HashSet 去重，重复元素只保留一份")
    public void testHashSetDemo01() {
        String output = captureOutput(HashSetDemo01::demo);
        assertThat(output).contains("A");
        assertThat(output).contains("B");
        assertThat(output).contains("D");
        assertThat(output).contains("E");
        assertThat(output).isEqualTo("[A, B, C, D, E]\n");
    }

    @Test
    @DisplayName("TreeSet 有序去重与范围查询")
    public void testTreeSetDemo() {
        String output = captureOutput(TreeSetDemo::demo);
        assertThat(output).contains("第一个元素：A");
        assertThat(output).contains("最后一个元素：E");
        assertThat(output).contains("headSet元素：[A, B]");
        assertThat(output).contains("tailSet元素：[C, D, E]");
        assertThat(output).contains("subSet元素：[B, C]");
    }

    @Test
    @DisplayName("TreeSet 存放自定义对象：按 Comparable 排序并去重")
    public void testTreeSetDemo2() {
        String output = captureOutput(TreeSetDemo2::demo);
        assertThat(output).contains("姓名：张三；年龄：30");
        assertThat(output).contains("姓名：李四；年龄：31");
        assertThat(output).contains("姓名：王五；年龄：32");
        assertThat(output).contains("姓名：孙七；年龄：33");
        assertThat(output).contains("姓名：赵六；年龄：33");
        // 三个王五只保留一份
        assertThat(output).containsOnlyOnce("姓名：王五；年龄：32");
        // 孙七与赵六同岁，按名字 Unicode 比较，孙七在前
        assertThat(output.indexOf("孙七")).isLessThan(output.indexOf("赵六"));
    }

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

}
