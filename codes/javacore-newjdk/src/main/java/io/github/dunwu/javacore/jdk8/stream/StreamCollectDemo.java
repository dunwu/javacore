package io.github.dunwu.javacore.jdk8.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java 8 Stream collect 收集器示例。
 * <p>
 * {@code collect} 是最常用的终结操作，配合 {@link Collectors} 工具类
 * 可以把流元素归集为集合、Map、字符串或统计结果：
 * <ul>
 * <li>{@code toList / toSet / toCollection}：归集为集合</li>
 * <li>{@code toMap}：归集为 Map（注意 key 冲突处理）</li>
 * <li>{@code joining}：拼接字符串</li>
 * <li>{@code counting / summing / averaging / maxBy / minBy / summarizing}：聚合统计</li>
 * </ul>
 */
public class StreamCollectDemo {

    /**
     * 示例 1：toList / toSet / toCollection 归集为集合
     */
    public static void toCollection() {
        List<String> words = Arrays.asList("Java", "Go", "Kotlin", "Java", "C");
        List<String> toList = words.stream().collect(Collectors.toList());
        System.out.println("toList: " + toList);
        System.out.println("toSet 去重: " + words.stream().collect(Collectors.toSet()).size() + " 个");
    }

    /**
     * 示例 2：toMap 归集为 Map，key 冲突时必须提供合并函数，否则抛 IllegalStateException；
     * 指定 LinkedHashMap 保持插入顺序，输出结果稳定
     */
    public static void toMapDemo() {
        List<String> words = Arrays.asList("Java", "Go", "Kotlin", "Java", "C");
        Map<String, Integer> wordLength = words.stream()
            .collect(Collectors.toMap(w -> w, String::length, (a, b) -> a, LinkedHashMap::new));
        System.out.println("toMap 单词长度: " + wordLength);
    }

    /**
     * 示例 3：joining 拼接为字符串
     */
    public static void joiningDemo() {
        List<String> words = Arrays.asList("Java", "Go", "Kotlin", "Java", "C");
        String joined = words.stream().collect(Collectors.joining(", ", "[", "]"));
        System.out.println("joining: " + joined);
    }

    /**
     * 示例 4：聚合统计（counting/summing/averaging/maxBy/summarizing）
     */
    public static void statistics() {
        List<String> words = Arrays.asList("Java", "Go", "Kotlin", "Java", "C");
        System.out.println("counting: " + words.stream().collect(Collectors.counting()));
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("summingInt: " + numbers.stream().collect(Collectors.summingInt(Integer::intValue)));
        System.out.println("averagingDouble: " + numbers.stream().collect(Collectors.averagingDouble(Integer::intValue)));
        Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Integer::compareTo));
        System.out.println("maxBy: " + max.orElse(-1));

        // summarizingInt：一次拿到个数/总和/最小/平均/最大
        IntSummaryStatistics stats = numbers.stream().collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println("summarizing: count=" + stats.getCount() + ", sum=" + stats.getSum()
            + ", min=" + stats.getMin() + ", max=" + stats.getMax());
    }

    public static void main(String[] args) {
        toCollection();
        toMapDemo();
        joiningDemo();
        statistics();
    }

}
// Output:
// toList: [Java, Go, Kotlin, Java, C]
// toSet 去重: 4 个
// toMap 单词长度: {Java=4, Go=2, Kotlin=6, C=1}
// joining: [Java, Go, Kotlin, Java, C]
// counting: 5
// summingInt: 15
// averagingDouble: 3.0
// maxBy: 5
// summarizing: count=5, sum=15, min=1, max=5
