package io.github.dunwu.javacore.jdk8.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Java 8 StringJoiner 与 String.join 示例。
 * <p>
 * Java 8 之前拼接字符串要手动处理分隔符和首尾边界，
 * Java 8 提供了两个标准方案：
 * <ul>
 * <li>{@link StringJoiner}：可设置分隔符、前缀、后缀，支持逐个 add</li>
 * <li>{@code String.join(分隔符, 元素...)}：静态便捷方法</li>
 * <li>{@code Collectors.joining}：Stream 中的等价方案</li>
 * </ul>
 * StringBuilder 底层由 {@code AbstractStringBuilder} 实现，StringJoiner 内部即基于它。
 */
public class StringJoinerDemo {

    /**
     * 示例 1：基础用法与带前缀后缀（常用于拼接 SQL 的 IN 条件、JSON 数组等）
     */
    public static void basicAndBracketed() {
        // 只设置分隔符
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add("Java").add("Go").add("Kotlin");
        System.out.println("StringJoiner: " + joiner);

        // 带前缀后缀
        StringJoiner bracketed = new StringJoiner(" | ", "[", "]");
        bracketed.add("a").add("b").add("c");
        System.out.println("带前后缀: " + bracketed);
    }

    /**
     * 示例 2：空值处理（setEmptyValue）与 merge 合并
     */
    public static void emptyValueAndMerge() {
        StringJoiner emptyJoiner = new StringJoiner(", ").setEmptyValue("无数据");
        System.out.println("空 Joiner: " + emptyJoiner);

        StringJoiner merged = new StringJoiner(", ").merge(new StringJoiner(", ").add("x").add("y"));
        System.out.println("merge 合并: " + merged);
    }

    /**
     * 示例 3：String.join 静态方法与 Stream + Collectors.joining
     */
    public static void joinMethods() {
        System.out.println("String.join: " + String.join("-", "2024", "05", "20"));
        List<String> list = Arrays.asList("Java", "8");
        System.out.println("String.join 集合: " + String.join(" + ", list));

        // Collectors.joining 与 StringJoiner 等价
        System.out.println("Collectors.joining: "
            + list.stream().collect(Collectors.joining(" + ", "[", "]")));
    }

    public static void main(String[] args) {
        basicAndBracketed();
        emptyValueAndMerge();
        joinMethods();
    }

}
// Output:
// StringJoiner: Java, Go, Kotlin
// 带前后缀: [a | b | c]
// 空 Joiner: 无数据
// merge 合并: x, y
// String.join: 2024-05-20
// String.join 集合: Java + 8
// Collectors.joining: [Java + 8]
