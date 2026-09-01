package io.github.dunwu.javacore.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Java 8 并行流（Parallel Stream）示例。
 * <p>
 * Java 8 的 Stream 可以一行代码切换串行/并行执行，底层基于 Fork/Join 框架：
 * <ul>
 * <li>{@code parallelStream()} 或 {@code stream().parallel()} 开启并行</li>
 * <li>{@code sequential()} 切回串行；同一个流上最后一次的切换调用生效</li>
 * <li>并行流把数据分片后在 {@link ForkJoinPool}（默认线程数 = CPU 核数）中计算，再合并结果</li>
 * </ul>
 * 适用场景：数据量大、计算密集、元素间无依赖；
 * 注意：并行不保证元素处理顺序，需要顺序输出时使用 {@code forEachOrdered}。
 */
public class StreamParallelDemo {

    /**
     * 示例 1：串行与并行结果一致（求和）
     */
    public static void serialVsParallelSum() {
        long serialSum = IntStream.rangeClosed(1, 100_0000).sum();
        long parallelSum = IntStream.rangeClosed(1, 100_0000).parallel().sum();
        System.out.println("串行求和 == 并行求和: " + (serialSum == parallelSum));
    }

    /**
     * 示例 2：并行切换，parallel() 与 sequential() 最后一次调用生效
     */
    public static void parallelSwitch() {
        boolean isParallel = IntStream.range(0, 10).parallel().sequential().isParallel();
        System.out.println("parallel().sequential() 后是否并行: " + isParallel);
    }

    /**
     * 示例 3：顺序对比，forEach 不保证顺序，forEachOrdered 保证遭遇顺序
     */
    public static void orderedTraversal() {
        List<Integer> parallelList = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
        List<Integer> ordered = new ArrayList<>();
        parallelList.parallelStream().forEachOrdered(ordered::add);
        System.out.println("forEachOrdered 保持顺序: " + ordered);
    }

    /**
     * 示例 4：并行收益演示，大数组求平方和
     */
    public static void parallelBenefit() {
        int size = 1000_0000;
        long start = System.currentTimeMillis();
        long serialResult = IntStream.rangeClosed(1, size).mapToLong(i -> (long) i * i).sum();
        long serialCost = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        long parallelResult = IntStream.rangeClosed(1, size).parallel().mapToLong(i -> (long) i * i).sum();
        long parallelCost = System.currentTimeMillis() - start;
        System.out.println("结果一致: " + (serialResult == parallelResult));
        System.out.println("串行耗时: " + serialCost + " ms, 并行耗时: " + parallelCost + " ms");

        // 并行流的常见陷阱提示（不演示错误，只做说明）：
        // - 有状态的 lambda（如往共享 ArrayList 中 add）会线程不安全，应使用 collect
        // - 对 LinkedList、IO 密集操作等并行往往得不偿失
    }

    public static void main(String[] args) {
        serialVsParallelSum();
        parallelSwitch();
        orderedTraversal();
        parallelBenefit();
    }

}
// Output:
// 串行求和 == 并行求和: true
// parallel().sequential() 后是否并行: false
// forEachOrdered 保持顺序: [1, 2, 3, 4, 5]
// 结果一致: true
// 串行耗时: ? ms, 并行耗时: ? ms（耗时因机器而异，并行通常不慢于串行）
