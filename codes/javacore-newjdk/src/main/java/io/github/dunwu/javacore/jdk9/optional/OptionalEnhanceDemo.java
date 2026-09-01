package io.github.dunwu.javacore.jdk9.optional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java 9 Optional 增强示例。
 * <p>
 * Java 9 为 {@link Optional} 新增了三个方法：
 * <ul>
 * <li>{@code ifPresentOrElse(Consumer, Runnable)}：值存在时执行 Consumer，否则执行 Runnable</li>
 * <li>{@code or(Supplier)}：值不存在时返回另一个 Optional（惰性求值）</li>
 * <li>{@code stream()}：将 Optional 转换为 0 或 1 个元素的 Stream，便于配合 Stream API 使用</li>
 * </ul>
 */
public class OptionalEnhanceDemo {

    /**
     * 示例 1：ifPresentOrElse——值存在或不存在时分别执行不同逻辑
     */
    public static void ifPresentOrElseDemo() {
        Optional.of("Java").ifPresentOrElse(
            value -> System.out.println("找到值: " + value),
            () -> System.out.println("值不存在"));
        Optional.empty().ifPresentOrElse(
            value -> System.out.println("找到值: " + value),
            () -> System.out.println("值不存在"));
    }

    /**
     * 示例 2：or——值不存在时，提供另一个 Optional 作为备选（Supplier 惰性求值）
     */
    public static void orDemo() {
        Optional<String> result = Optional.<String>empty()
            .or(() -> Optional.of("默认值"));
        System.out.println("or 备选结果: " + result.orElse("无"));
    }

    /**
     * 示例 3：stream——把 Optional 转成 Stream，适合批量处理一组 Optional
     */
    public static void streamDemo() {
        List<Optional<String>> optionals = List.of(
            Optional.of("a"), Optional.empty(), Optional.of("b"), Optional.empty());
        String joined = optionals.stream()
            .flatMap(Optional::stream)
            .collect(Collectors.joining(", "));
        System.out.println("Optional.stream 拼接结果: " + joined);
    }

    public static void main(String[] args) {
        ifPresentOrElseDemo();
        orDemo();
        streamDemo();
    }

}
// Output:
// 找到值: Java
// 值不存在
// or 备选结果: 默认值
// Optional.stream 拼接结果: a, b
