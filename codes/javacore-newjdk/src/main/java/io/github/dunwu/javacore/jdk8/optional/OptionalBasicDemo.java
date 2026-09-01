package io.github.dunwu.javacore.jdk8.optional;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Java 8 Optional 基础用法示例。
 * <p>
 * {@link Optional} 是一个容器类，用于优雅地表达"值可能缺失"，
 * 替代到处判空的 {@code if (x != null)} 代码，主要用作方法返回值。
 * <ul>
 * <li>{@code Optional.of(v)}：包装非空值，传 null 立即抛 NPE</li>
 * <li>{@code Optional.ofNullable(v)}：值可为 null，为 null 时返回 empty</li>
 * <li>{@code Optional.empty()}：空 Optional</li>
 * <li>{@code isPresent / get / orElse / orElseGet / orElseThrow}：取值</li>
 * </ul>
 * 注意：不建议用 Optional 做字段、方法参数或集合元素。
 */
public class OptionalBasicDemo {

    /**
     * 示例 1：三种创建方式（of/ofNullable/empty）
     */
    public static void createOptional() {
        Optional<String> present = Optional.of("Java");
        Optional<String> nullable = Optional.ofNullable(null);
        Optional<String> empty = Optional.empty();
        System.out.println("of: " + present.get());
        System.out.println("ofNullable(null) isPresent: " + nullable.isPresent());
        System.out.println("empty 无值: " + !empty.isPresent());
    }

    /**
     * 示例 2：orElse（固定默认值）与 orElseGet（惰性默认值）的区别
     */
    public static void orElseVsOrElseGet() {
        Optional<String> nullable = Optional.ofNullable(null);
        Optional<String> present = Optional.of("Java");
        System.out.println("orElse: " + nullable.orElse("默认值"));
        System.out.println("orElseGet: " + nullable.orElseGet(() -> "惰性计算的默认值"));
        // 区别：值存在时 orElse 的参数仍会被计算，orElseGet 不会执行 supplier
        System.out.println("orElse 即使有值也计算参数: " + present.orElse(computeDefault()));
        System.out.println("orElseGet 有值时不计算: " + present.orElseGet(OptionalBasicDemo::computeDefault));
    }

    /**
     * 示例 3：orElseThrow，值缺失时抛出自定义异常
     */
    public static void orElseThrowDemo() {
        Optional<String> empty = Optional.empty();
        try {
            empty.orElseThrow(() -> new NoSuchElementException("自定义：值不存在"));
        } catch (NoSuchElementException e) {
            System.out.println("orElseThrow: " + e.getMessage());
        }
    }

    /**
     * 示例 4：ifPresent 与 Optional.of(null) 直接抛 NPE
     */
    public static void ifPresentAndOfNull() {
        Optional<String> present = Optional.of("Java");
        present.ifPresent(v -> System.out.println("ifPresent: " + v));

        // Optional 不能包装 null 进 of()
        try {
            Optional.of(null);
        } catch (NullPointerException e) {
            System.out.println("Optional.of(null) 抛出 NullPointerException");
        }
    }

    public static void main(String[] args) {
        createOptional();
        orElseVsOrElseGet();
        orElseThrowDemo();
        ifPresentAndOfNull();
    }

    private static String computeDefault() {
        System.out.println("  [computeDefault 被调用]");
        return "计算出的默认值";
    }

}
// Output:
// of: Java
// ofNullable(null) isPresent: false
// empty 无值: true
// orElse: 默认值
// orElseGet: 惰性计算的默认值
//   [computeDefault 被调用]
// orElse 即使有值也计算参数: Java
// orElseGet 有值时不计算: Java
// orElseThrow: 自定义：值不存在
// ifPresent: Java
// Optional.of(null) 抛出 NullPointerException
