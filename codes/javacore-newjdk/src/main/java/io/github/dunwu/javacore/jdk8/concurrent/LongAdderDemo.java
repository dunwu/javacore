package io.github.dunwu.javacore.jdk8.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * Java 8 高性能计数器（LongAdder / LongAccumulator）示例。
 * <p>
 * Java 8 在 {@code java.util.concurrent.atomic} 中新增了分段计数器：
 * <ul>
 * <li>{@link LongAdder}：高并发下比 {@link AtomicLong} 快得多。AtomicLong 所有线程竞争
 * 同一个 CAS 位，竞争激烈时大量自旋空转；LongAdder 将计数分散到多个 Cell 上，
 * 写时近乎无竞争，只在 {@code sum()} 时汇总（牺牲实时精确性换吞吐量）</li>
 * <li>{@link LongAccumulator}：通用版，支持自定义二元累积函数（如求最大值）</li>
 * </ul>
 * 适用场景：统计计数（如请求数），读少写多；不适用于需要精确实时值的场景（如序列号）。
 */
public class LongAdderDemo {

    private static final int THREADS = 8;

    private static final int TIMES_PER_THREAD = 100_0000;

    /**
     * 示例 1：LongAdder 基础用法（increment/add/sum/sumThenReset）
     */
    public static void basicUsage() {
        LongAdder adder = new LongAdder();
        adder.increment();
        adder.add(10);
        System.out.println("LongAdder sum: " + adder.sum() + ", sumThenReset: " + adder.sumThenReset());
        System.out.println("reset 后 sum: " + adder.sum());
    }

    /**
     * 示例 2：高并发累加正确性验证，8 线程各累加 100 万次
     */
    public static void concurrentAccuracy() throws InterruptedException {
        LongAdder concurrentAdder = new LongAdder();
        runConcurrently(concurrentAdder::increment);
        System.out.println("8 线程并发累加结果: " + concurrentAdder.sum()
            + "（期望 " + (long) THREADS * TIMES_PER_THREAD + "）");
    }

    /**
     * 示例 3：LongAccumulator 自定义累积函数，并发求最大值
     */
    public static void accumulatorMax() throws InterruptedException {
        LongAccumulator maxAccumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        runConcurrently(() -> maxAccumulator.accumulate(Thread.currentThread().getId()));
        System.out.println("LongAccumulator 并发求最大值 >= 1: " + (maxAccumulator.get() >= 1));
    }

    /**
     * 示例 4：与 AtomicLong 的语义对比，二者最终结果一致，差异在竞争时的性能
     */
    public static void compareWithAtomicLong() throws InterruptedException {
        AtomicLong atomicLong = new AtomicLong();
        runConcurrently(atomicLong::incrementAndGet);
        System.out.println("AtomicLong 并发累加结果: " + atomicLong.get());
    }

    public static void main(String[] args) throws InterruptedException {
        basicUsage();
        concurrentAccuracy();
        accumulatorMax();
        compareWithAtomicLong();
    }

    private static void runConcurrently(Runnable task) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            pool.execute(() -> {
                for (int j = 0; j < TIMES_PER_THREAD; j++) {
                    task.run();
                }
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();
    }

}
// Output:
// LongAdder sum: 11, sumThenReset: 11
// reset 后 sum: 0
// 8 线程并发累加结果: 8000000（期望 8000000）
// LongAccumulator 并发求最大值 >= 1: true
// AtomicLong 并发累加结果: 8000000
