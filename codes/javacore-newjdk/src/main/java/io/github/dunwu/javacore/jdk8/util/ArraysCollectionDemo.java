package io.github.dunwu.javacore.jdk8.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;

/**
 * Java 8 Arrays 工具类与集合批量操作增强示例。
 * <p>
 * Java 8 对数组和集合的常用操作做了函数式升级：
 * <ul>
 * <li>{@code Arrays.parallelSort}：并行排序（Fork/Join 实现）</li>
 * <li>{@code Arrays.setAll / fill}：用函数生成数组元素</li>
 * <li>{@code Arrays.stream}：数组转流</li>
 * <li>{@code spliterator()}：可分割迭代器，并行流的底层支撑</li>
 * <li>集合批量操作：{@code forEach / removeIf / sort / Iterator.remove}</li>
 * </ul>
 */
public class ArraysCollectionDemo {

    /**
     * 示例 1：Arrays.parallelSort 并行排序、setAll 按索引函数填充、stream 转流
     */
    public static void arraysEnhance() {
        // parallelSort：大数据量下比 Arrays.sort 更快
        int[] numbers = {5, 3, 8, 1, 9, 2};
        Arrays.parallelSort(numbers);
        System.out.println("parallelSort: " + Arrays.toString(numbers));

        // setAll：按索引函数填充数组（生成平方数）
        int[] squares = new int[5];
        Arrays.setAll(squares, i -> i * i);
        System.out.println("setAll 平方数: " + Arrays.toString(squares));

        // Arrays.stream：数组直接转流
        System.out.println("Arrays.stream 求和: " + Arrays.stream(numbers).sum());
    }

    /**
     * 示例 2：Spliterator 可分割迭代器，支持 tryAdvance 逐个消费、trySplit 对半拆分
     */
    public static void spliteratorDemo() {
        Spliterator<String> spliterator = Arrays.asList("a", "b", "c", "d").spliterator();
        Spliterator<String> half = spliterator.trySplit();
        StringBuilder left = new StringBuilder();
        half.forEachRemaining(left::append);
        StringBuilder right = new StringBuilder();
        spliterator.forEachRemaining(right::append);
        System.out.println("spliterator 拆分: [" + left + "] 和 [" + right + "]");
    }

    /**
     * 示例 3：集合批量操作（forEach/removeIf/sort）与 Stream 过滤对比
     */
    public static void collectionBatchOps() {
        List<String> languages = new ArrayList<>(Arrays.asList("Java", "Go", "Kotlin", "C"));
        // Collection.forEach：方法引用遍历
        System.out.print("forEach: ");
        languages.forEach(lang -> System.out.print(lang + " "));
        System.out.println();

        // removeIf：按条件批量删除（替代迭代器手写删除）
        languages.removeIf(lang -> lang.length() <= 2);
        System.out.println("removeIf 短语言: " + languages);

        // List.sort：原地排序，接受 Comparator
        languages.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("sort 按长度倒序: " + languages);

        // Stream 过滤对比
        List<String> filtered = languages.stream()
            .filter(lang -> lang.startsWith("J"))
            .collect(Collectors.toList());
        System.out.println("stream filter: " + filtered);
    }

    public static void main(String[] args) {
        arraysEnhance();
        spliteratorDemo();
        collectionBatchOps();
    }

}
// Output:
// parallelSort: [1, 2, 3, 5, 8, 9]
// setAll 平方数: [0, 1, 4, 9, 16]
// Arrays.stream 求和: 28
// spliterator 拆分: [ab] 和 [cd]
// forEach: Java Go Kotlin C
// removeIf 短语言: [Java, Kotlin]
// sort 按长度倒序: [Kotlin, Java]
// stream filter: [Java]
