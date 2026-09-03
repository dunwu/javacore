package io.github.dunwu.javacore.concurrent.atomic;

import io.github.dunwu.javacore.concurrent.annotation.Error;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 错误示例：卖票时不做任何线程安全控制
 * <p>
 * {@code ticket} 是共享的静态变量，{@code while (ticket > 0)} 的判断、打印与 {@code ticket--} 三步之间没有任何同步，
 * 因此多个线程会读到相同的 {@code ticket} 值、卖出同一张票（输出里会出现重复的票号），
 * {@code ticket} 还可能被减成负数。
 * <p>
 * 对比 {@link AtomicReferenceDemo2}：用 {@link java.util.concurrent.atomic.AtomicReference} 实现的自旋锁
 * 把「判断 + 打印 + 减一」包成临界区后，每张票只会被卖出一次。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see AtomicReferenceDemo2
 */
@Error
public class AtomicReferenceDemo {

    private static int ticket = 10;

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 5 个任务在 3 个线程上并发卖 10 张票，因缺少同步会出现重复票号。
     * 末尾的 {@code awaitTermination} 保证方法返回前所有输出已经打印完毕
     */
    public static void demo() throws InterruptedException {
        // 重置票数，保证本方法可以重复调用（否则第二次调用时 ticket 已不大于 0，不会有任何输出）
        ticket = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 不做任何线程安全控制
     */
    static class MyThread implements Runnable {

        @Override
        public void run() {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
