package io.github.dunwu.javacore.concurrent.tool.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 示例：控制同时访问临界区的线程个数
 * <p>
 * 字面意思为「信号量」。{@code Semaphore} 维护一组许可（permit）：
 * {@code acquire()} 取走一个许可，没有就阻塞等待；{@code release()} 归还一个许可，并唤醒一个等待者。
 * 与互斥锁只能一个线程进入不同，{@code new Semaphore(10)} 允许最多 10 个线程同时进入，
 * 因此适合做「限流」、「连接池」、「对象池」这类需要控制并发度而不是完全互斥的场景（参见 {@link io.github.dunwu.javacore.concurrent.tool.SemaphoreRateLimit}）。
 * <p>
 * 本例用 30 个线程、但只给 10 个许可，因此同一时刻最多 10 个线程在打印，其余 20 个在 {@code acquire()} 上排队。
 * <p>
 * 关键细节：{@code release()} 必须放在 {@code finally} 里。本例临界区不会抛异常，所以写在后面也能跑，
 * 但真实代码中一旦 {@code acquire()} 与 {@code release()} 之间抛了异常，许可就永久丢失，
 * 最终会没有任何线程能再获取到许可，造成整个系统假死。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see java.util.concurrent.Semaphore
 * @see io.github.dunwu.javacore.concurrent.tool.SemaphoreRateLimit
 * @since 2018/5/10
 */
public class SemaphoreDemo {

    private static final int THREAD_COUNT = 30;
    private static Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 30 个线程争抢 10 个许可，每个拿到许可的线程打印一行 {@code save data}，因此输出恰好是 30 行相同的内容。
     * 行的先后顺序取决于调度，但内容完全一致，所以整体输出是确定的。
     * 末尾的 {@code awaitTermination} 保证方法返回前 30 行已全部打印完毕
     */
    public static void demo() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("save data");
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

}
