package io.github.dunwu.javacore.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java 8 Stream 创建方式示例。
 * <p>
 * Stream 是 Java 8 引入的声明式数据处理管道（JEP 107），
 * 它不存储数据、不修改数据源，只做计算，且<b>只能消费一次</b>。
 * <p>
 * 常见创建方式：
 * <ul>
 * <li>集合的 {@code stream()} / {@code parallelStream()}</li>
 * <li>{@code Stream.of(...)}、数组的 {@code Arrays.stream(...)}</li>
 * <li>{@code Stream.iterate}（无限流）、{@code Stream.generate}（无限流）</li>
 * <li>基本类型专用流：{@code IntStream.range} 等</li>
 * </ul>
 */
public class StreamCreateDemo {

    /**
     * 示例 1：从集合创建，以及 Stream.of / Arrays.stream
     */
    public static void fromCollectionAndOf() {
        // 1. 从集合创建
        List<String> list = Arrays.asList("a", "b", "c");
        System.out.println("集合创建: " + list.stream().count() + " 个元素");

        // 2. Stream.of / Arrays.stream
        System.out.println("Stream.of: " + Stream.of(1, 2, 3).count());
        int[] array = {4, 5, 6};
        System.out.println("Arrays.stream 求和: " + Arrays.stream(array).sum());
    }

    /**
     * 示例 2：iterate 与 generate 无限流，配合 limit 截取
     */
    public static void iterateAndGenerate() {
        // 3. iterate：无限流（种子 + 递推函数），配合 limit 截取
        List<Integer> evens = Stream.iterate(0, n -> n + 2).limit(5).collect(Collectors.toList());
        System.out.println("iterate 生成偶数: " + evens);

        // 4. generate：无限流（Supplier），常用于随机数
        Random random = new Random(42);
        List<Integer> randoms = Stream.generate(() -> random.nextInt(100))
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("generate 生成随机数（固定种子）: " + randoms);
    }

    /**
     * 示例 3：基本类型专用流，避免装箱开销
     */
    public static void primitiveStream() {
        System.out.println("IntStream.range(1, 6) 求和: " + IntStream.range(1, 6).sum());
        System.out.println("IntStream.rangeClosed(1, 6) 求和: " + IntStream.rangeClosed(1, 6).sum());
    }

    /**
     * 示例 4：字符串转字符流
     */
    public static void charsStream() {
        System.out.println("chars 统计字母个数: " + "Java8".chars().filter(Character::isLetter).count());
    }

    /**
     * 示例 5：空流
     */
    public static void emptyStream() {
        System.out.println("Stream.empty: " + Stream.empty().count());
    }

    /**
     * 示例 6：Stream 只能消费一次，二次使用抛 IllegalStateException
     */
    public static void consumeOnce() {
        Stream<Integer> consumed = Stream.of(1);
        consumed.count();
        try {
            consumed.count();
        } catch (IllegalStateException e) {
            System.out.println("Stream 已消费，二次使用抛出 IllegalStateException");
        }
    }

    public static void main(String[] args) {
        fromCollectionAndOf();
        iterateAndGenerate();
        primitiveStream();
        charsStream();
        emptyStream();
        consumeOnce();
    }

}
// Output:
// 集合创建: 3 个元素
// Stream.of: 3
// Arrays.stream 求和: 15
// iterate 生成偶数: [0, 2, 4, 6, 8]
// generate 生成随机数（固定种子）: [30, 63, 48]
// IntStream.range(1, 6) 求和: 15
// IntStream.rangeClosed(1, 6) 求和: 21
// chars 统计字母个数: 4
// Stream.empty: 0
// Stream 已消费，二次使用抛出 IllegalStateException
