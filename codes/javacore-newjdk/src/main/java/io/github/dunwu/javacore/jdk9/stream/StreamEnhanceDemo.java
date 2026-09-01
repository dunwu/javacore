package io.github.dunwu.javacore.jdk9.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 9 Stream 增强示例。
 * <p>
 * Java 9 为 {@link Stream} 新增了四个方法：
 * <ul>
 * <li>{@code takeWhile(Predicate)}：从流头部开始取元素，直到第一个不满足条件的元素为止（与 filter 不同，不会遍历整个流）</li>
 * <li>{@code dropWhile(Predicate)}：从流头部开始丢弃元素，直到第一个不满足条件的元素为止</li>
 * <li>{@code ofNullable(T)}：把可能为 null 的值安全地转换为流</li>
 * <li>{@code iterate(T, Predicate, UnaryOperator)}：带终止条件的 iterate，避免无限流</li>
 * </ul>
 */
public class StreamEnhanceDemo {

    /**
     * 示例 1：takeWhile / dropWhile——按条件截取或丢弃流的前缀
     */
    public static void takeWhileAndDropWhile() {
        // takeWhile：一旦遇到不满足条件的元素就停止
        List<Integer> taken = Stream.of(1, 3, 5, 2, 4, 6)
            .takeWhile(n -> n % 2 == 1)
            .collect(Collectors.toList());
        System.out.println("takeWhile 奇数前缀: " + taken);

        // dropWhile：丢弃满足条件的前缀，保留剩余部分
        List<Integer> dropped = Stream.of(1, 3, 5, 2, 4, 6)
            .dropWhile(n -> n % 2 == 1)
            .collect(Collectors.toList());
        System.out.println("dropWhile 丢弃奇数前缀: " + dropped);
    }

    /**
     * 示例 2：ofNullable——null 值转换为空流，避免 NullPointerException
     */
    public static void ofNullableDemo() {
        long nullCount = Stream.ofNullable(null).count();
        long valueCount = Stream.ofNullable("Java").count();
        System.out.println("ofNullable(null) 元素个数: " + nullCount);
        System.out.println("ofNullable(\"Java\") 元素个数: " + valueCount);
    }

    /**
     * 示例 3：带终止条件的 iterate——生成 1、2、4、8...（小于 100）
     */
    public static void iterateWithPredicate() {
        List<Integer> powers = Stream.iterate(1, n -> n < 100, n -> n * 2)
            .collect(Collectors.toList());
        System.out.println("iterate 带终止条件: " + powers);
    }

    public static void main(String[] args) {
        takeWhileAndDropWhile();
        ofNullableDemo();
        iterateWithPredicate();
    }

}
// Output:
// takeWhile 奇数前缀: [1, 3, 5]
// dropWhile 丢弃奇数前缀: [2, 4, 6]
// ofNullable(null) 元素个数: 0
// ofNullable("Java") 元素个数: 1
// iterate 带终止条件: [1, 2, 4, 8, 16, 32, 64]
