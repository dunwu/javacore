package io.github.dunwu.javacore.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java 8 Stream 终结操作与归约示例。
 * <p>
 * 终结操作（terminal operation）消费流并产生结果，执行后流即关闭：
 * <ul>
 * <li>{@code forEach}：遍历元素（并行流中不保证顺序，需要顺序用 forEachOrdered）</li>
 * <li>{@code reduce}：把元素反复"折叠"为一个值（求和、求积、求最值）</li>
 * <li>{@code min / max / count}：聚合</li>
 * <li>匹配与查找：{@code anyMatch / allMatch / noneMatch / findFirst / findAny}</li>
 * </ul>
 */
public class StreamReduceDemo {

    /**
     * 示例 1：forEach 遍历（串行流中按遭遇顺序）
     */
    public static void forEachDemo() {
        System.out.print("forEach: ");
        Stream.of(1, 2, 3).forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    /**
     * 示例 2：reduce 三种形式
     */
    public static void reduceDemo() {
        // (1) Optional<T> reduce(BinaryOperator)：流可能为空
        Optional<Integer> sum = Stream.of(1, 2, 3, 4).reduce(Integer::sum);
        System.out.println("reduce 求和: " + sum.orElse(0));
        // (2) T reduce(identity, BinaryOperator)：提供初始值
        int product = Stream.of(1, 2, 3, 4).reduce(1, (a, b) -> a * b);
        System.out.println("reduce 求积: " + product);
        // (3) U reduce(identity, accumulator, combiner)：并行流安全版，
        // accumulator 把元素累加到结果上，combiner 合并并行子结果
        int totalLength = Stream.of("a", "bb", "ccc").reduce(0,
            (acc, s) -> acc + s.length(), Integer::sum);
        System.out.println("reduce 总长度: " + totalLength);
    }

    /**
     * 示例 3：min / max / count 聚合
     */
    public static void minMaxCount() {
        OptionalInt max = IntStream.of(3, 1, 4, 1, 5).max();
        System.out.println("max: " + max.getAsInt() + ", count: " + IntStream.of(3, 1, 4, 1, 5).count());
    }

    /**
     * 示例 4：匹配操作（短路：找到结果即停止计算）
     */
    public static void matchDemo() {
        List<Integer> numbers = Arrays.asList(2, 4, 6, 8);
        System.out.println("allMatch 偶数: " + numbers.stream().allMatch(n -> n % 2 == 0));
        System.out.println("anyMatch 大于 7: " + numbers.stream().anyMatch(n -> n > 7));
        System.out.println("noneMatch 负数: " + numbers.stream().noneMatch(n -> n < 0));
    }

    /**
     * 示例 5：查找操作（短路）
     */
    public static void findDemo() {
        Optional<Integer> first = Stream.of(5, 6, 7).filter(n -> n % 2 == 0).findFirst();
        System.out.println("findFirst: " + first.orElse(-1));
        // findAny 在串行流中通常等价于 findFirst，在并行流中可更快返回
        Optional<Integer> any = Stream.of(5, 6, 7).filter(n -> n % 2 == 0).findAny();
        System.out.println("findAny: " + any.orElse(-1));
    }

    public static void main(String[] args) {
        forEachDemo();
        reduceDemo();
        minMaxCount();
        matchDemo();
        findDemo();
    }

}
// Output:
// forEach: 1 2 3
// reduce 求和: 10
// reduce 求积: 24
// reduce 总长度: 6
// max: 5, count: 5
// allMatch 偶数: true
// anyMatch 大于 7: true
// noneMatch 负数: true
// findFirst: 6
// findAny: 6
