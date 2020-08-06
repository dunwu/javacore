package io.github.dunwu.javacore.concurrent.container;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * 解决了 WrongConcurrentHashMapDemo 的数据不一致问题，但使用 synchronized ，不能真正发挥 ConcurrentHashMap 的特性
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see WrongConcurrentHashMapDemo
 * @since 2018/5/16
 */
public class WrongConcurrentHashMapDemo2 {

    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        //初始900个元素
        System.out.println("init size:" + concurrentHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            //查询还需要补充多少个元素
            synchronized (concurrentHashMap) {
                int gap = ITEM_COUNT - concurrentHashMap.size();
                System.out.println("gap size:" + gap);
                //补充元素
                concurrentHashMap.putAll(getData(gap));
            }
        }));
        //等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //最后元素个数会是1000吗？
        System.out.println("finish size:" + concurrentHashMap.size());
    }

    private static ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
            .boxed()
            .collect(
                Collectors.toConcurrentMap(
                    i -> UUID.randomUUID().toString(),
                    i -> i,
                    (o1, o2) -> o1,
                    ConcurrentHashMap::new));
    }

}
// Expect: finish size:1000
// Output: finish size:1000
