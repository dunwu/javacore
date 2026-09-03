package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link java.util.concurrent.atomic.AtomicInteger} 示例
 * <p>
 * 3 个线程的线程池执行 10 个任务，每个任务对 {@code count} 做一次 {@code incrementAndGet()}。
 * {@code incrementAndGet()} 基于 CAS 自旋，是原子操作，因此不论线程如何调度，结果都稳定为 10。
 * 对比 {@link io.github.dunwu.javacore.concurrent.error.WrongResult}（{@code volatile} + {@code i++} 结果不可控）。
 * <p>
 * 注意：{@code shutdown()} 只是不再接受新任务，不会等已提交的任务执行完，
 * 必须再调 {@code awaitTermination()} 阻塞到线程池真正结束，否则可能在任务跑完前就读取 {@code count}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 * @see io.github.dunwu.javacore.concurrent.error.WrongResult
 */
public class AtomicIntegerDemo {

    public static void demo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}
// Output:
// Final Count is : 10
