package io.github.dunwu.javacore.jdk11.optional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Java 11 Optional 与 Predicate 增强示例。
 * <p>
 * Java 11 新增：
 * <ul>
 * <li>{@code Optional.isEmpty()}：判断值是否不存在，与 {@code isPresent()} 互为反义，
 * 适合配合方法引用使用（如 {@code filter(Optional::isEmpty)}）</li>
 * <li>{@code Predicate.not(Predicate)}：生成逻辑取反的谓词，替代手写 {@code s -> !s.isBlank()}，
 * 与方法引用组合后代码更简洁</li>
 * </ul>
 */
public class OptionalEnhanceDemo {

    /**
     * 示例 1：isEmpty——判断 Optional 是否不持有任何值，可配合方法引用过滤空值
     */
    public static void isEmptyDemo() {
        System.out.println("Optional.empty().isEmpty(): " + Optional.empty().isEmpty());
        System.out.println("Optional.of(\"a\").isEmpty(): " + Optional.of("a").isEmpty());

        // isEmpty 配合方法引用：过滤出一组 Optional 中的空值
        List<Optional<String>> optionals = List.of(
            Optional.of("a"), Optional.empty(), Optional.of("b"));
        long emptyCount = optionals.stream()
            .filter(Optional::isEmpty)
            .count();
        System.out.println("空 Optional 个数: " + emptyCount);
    }

    /**
     * 示例 2：Predicate.not——逻辑取反的谓词，与传统手写取反结果一致
     */
    public static void predicateNotDemo() {
        List<String> words = List.of("Java", "", "  ", "Kotlin", "\t");
        List<String> nonBlank = words.stream()
            .filter(Predicate.not(String::isBlank))
            .collect(Collectors.toList());
        System.out.println("非空白元素: " + nonBlank);

        // 传统写法对比：需要手写 lambda 取反
        List<String> nonBlankOldStyle = words.stream()
            .filter(s -> !s.isBlank())
            .collect(Collectors.toList());
        System.out.println("传统写法结果一致: " + nonBlank.equals(nonBlankOldStyle));
    }

    public static void main(String[] args) {
        isEmptyDemo();
        predicateNotDemo();
    }

}
// Output:
// Optional.empty().isEmpty(): true
// Optional.of("a").isEmpty(): false
// 空 Optional 个数: 1
// 非空白元素: [Java, Kotlin]
// 传统写法结果一致: true
