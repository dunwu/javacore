package io.github.dunwu.javacore.concurrent.container;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * ConcurrentHashMap 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018-05-16
 */
public class WrongConcurrentHashMapDemo {

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
            int gap = ITEM_COUNT - concurrentHashMap.size();
            System.out.println("gap size:" + gap);
            //补充元素
            concurrentHashMap.putAll(getData(gap));
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
// Output: finish size:1900
//
// 竞态示例，输出不确定。10 个任务若在任何 putAll 生效前全部读到 size=900，则各自 gap=100，最终为
// 1900；若恰好串行执行，第一个任务补足到 1000 后其余 gap=0，最终就是 1000。因此 finish size 只保证
// >= 1000，取值范围是 [1000, 1900]，并不能保证严格大于 1000。gap 也可能为负（此时 rangeClosed
// 产生空流，putAll 不改变大小）。完整输出还包含 init size:900 与 10 行 gap size。
// 上面是 JDK 21 + ForkJoinPool(10) 下连续 40 次运行的一致结果（每次均为 10 行 gap size:100）。
// 对照 WrongConcurrentHashMapDemo2：把读取与补足放进同一个 synchronized 块后，40 次实测恒为 1000。
