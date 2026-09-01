package io.github.dunwu.javacore.jdk8.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 8 Stream 中间操作示例。
 * <p>
 * 中间操作（intermediate operation）返回新的 Stream，且是<b>惰性</b>的：
 * 只有遇到终结操作（如 forEach/collect）时才真正执行。
 * <ul>
 * <li>{@code filter}：按条件过滤</li>
 * <li>{@code map} / {@code mapToInt} 等：一对一转换</li>
 * <li>{@code flatMap}：一对多转换（把每个元素映射成一个流再拍平）</li>
 * <li>{@code distinct} / {@code sorted} / {@code limit} / {@code skip} / {@code peek}</li>
 * </ul>
 */
public class StreamOperationDemo {

    /**
     * 示例 1：filter 按条件过滤，map 一对一转换
     */
    public static void filterAndMap() {
        // filter：保留偶数
        List<Integer> evens = Stream.of(1, 2, 3, 4, 5, 6)
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("filter 偶数: " + evens);

        // map：元素转换
        List<Integer> lengths = Stream.of("Java", "Go", "Kotlin")
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println("map 求长度: " + lengths);
    }

    /**
     * 示例 2：flatMap 一对多转换（把每个元素映射成一个流再拍平）
     */
    public static void flatMapDemo() {
        List<String> letters = Stream.of("Hi", "Java")
            .flatMap(word -> Arrays.stream(word.split("")))
            .collect(Collectors.toList());
        System.out.println("flatMap 拍平: " + letters);
    }

    /**
     * 示例 3：distinct 去重与 sorted 排序
     */
    public static void distinctAndSorted() {
        // distinct：去重（基于 equals）
        List<Integer> distinct = Stream.of(1, 2, 2, 3, 3, 3)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("distinct 去重: " + distinct);

        // sorted：排序（自然序或自定义 Comparator）
        List<String> sorted = Stream.of("banana", "apple", "cherry")
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        System.out.println("sorted 按长度: " + sorted);
    }

    /**
     * 示例 4：limit / skip 截取与 peek 旁路观察，及惰性求值验证
     */
    public static void limitSkipPeek() {
        // limit / skip：截取与跳过
        List<Integer> window = Stream.iterate(1, n -> n + 1)
            .skip(2)
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("skip(2).limit(3): " + window);

        // peek：调试/旁路观察，不改变流中的元素
        List<Integer> peeked = Stream.of(1, 2, 3)
            .peek(n -> System.out.println("  peek 观察: " + n))
            .map(n -> n * 10)
            .collect(Collectors.toList());
        System.out.println("peek 后结果: " + peeked);

        // 惰性求值验证：没有终结操作时，peek 不会执行
        Stream.of(1, 2, 3).peek(n -> System.out.println("这行不会被打印"));
        System.out.println("惰性求值：中间操作未触发");
    }

    public static void main(String[] args) {
        filterAndMap();
        flatMapDemo();
        distinctAndSorted();
        limitSkipPeek();
    }

}
// Output:
// filter 偶数: [2, 4, 6]
// map 求长度: [4, 2, 6]
// flatMap 拍平: [H, i, J, a, v, a]
// distinct 去重: [1, 2, 3]
// sorted 按长度: [apple, banana, cherry]
// skip(2).limit(3): [3, 4, 5]
//   peek 观察: 1
//   peek 观察: 2
//   peek 观察: 3
// peek 后结果: [10, 20, 30]
// 惰性求值：中间操作未触发
