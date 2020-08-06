package io.github.dunwu.javacore.concurrent.container;

import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 解决了 WrongConcurrentHashMapDemo 的数据不一致问题，但使用 synchronized ，不能真正发挥 ConcurrentHashMap 的特性
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see WrongConcurrentHashMapDemo
 * @since 2018/5/16
 */
public class WrongConcurrentHashMapDemo3 {

    //循环次数
    private static int LOOP_COUNT = 10000000;
    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normaluse");
        Map<String, Long> normaluse = normaluse();
        stopWatch.stop();
        Assert.isTrue(normaluse.size() == ITEM_COUNT, "normaluse size error");
        Assert.isTrue(normaluse.values().stream()
                .mapToLong(aLong -> aLong).reduce(0, Long::sum) == LOOP_COUNT
            , "normaluse count error");
        stopWatch.start("gooduse");
        Map<String, Long> gooduse = gooduse();
        stopWatch.stop();
        Assert.isTrue(gooduse.size() == ITEM_COUNT, "gooduse size error");
        Assert.isTrue(gooduse.values().stream()
                .mapToLong(l -> l)
                .reduce(0, Long::sum) == LOOP_COUNT
            , "gooduse count error");
        System.out.println(stopWatch.prettyPrint());
    }

    private static Map<String, Long> normaluse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                synchronized (freqs) {
                    if (freqs.containsKey(key)) {
                        freqs.put(key, freqs.get(key) + 1);
                    } else {
                        freqs.put(key, 1L);
                    }
                }
            }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    private static Map<String, Long> gooduse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                freqs.computeIfAbsent(key, k -> new LongAdder()).increment();
            }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1L, TimeUnit.HOURS);
        return freqs.entrySet().stream()
            .collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().longValue())
            );
    }

}
