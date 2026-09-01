package io.github.dunwu.javacore.jdk8.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StringJoinerDemo} 单元测试。
 */
@DisplayName("Java 8 StringJoiner 与 String.join 示例测试")
public class StringJoinerDemoTest {

    @Test
    @DisplayName("示例 1：StringJoiner 基础用法与带前后缀")
    public void testBasicAndBracketed() {
        String output = captureOutput(StringJoinerDemo::basicAndBracketed);
        assertThat(output)
            .contains("StringJoiner: Java, Go, Kotlin")
            .contains("带前后缀: [a | b | c]");
    }

    @Test
    @DisplayName("示例 2：setEmptyValue 空值输出与 merge 合并")
    public void testEmptyValueAndMerge() {
        String output = captureOutput(StringJoinerDemo::emptyValueAndMerge);
        assertThat(output)
            .contains("空 Joiner: 无数据")
            .contains("merge 合并: x, y");
    }

    @Test
    @DisplayName("示例 3：String.join 静态方法与 Collectors.joining 等价")
    public void testJoinMethods() {
        String output = captureOutput(StringJoinerDemo::joinMethods);
        assertThat(output)
            .contains("String.join: 2024-05-20")
            .contains("String.join 集合: Java + 8")
            .contains("Collectors.joining: [Java + 8]");
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
