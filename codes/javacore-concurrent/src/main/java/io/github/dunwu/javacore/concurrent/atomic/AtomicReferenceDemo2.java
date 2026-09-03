package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 利用 {@link AtomicReference} 实现自旋锁
 * <p>
 * 思路：把锁的持有者（当前线程）存进 {@code AtomicReference<Thread>}，初始为 {@code null}。
 * 加锁就是不停地 CAS：把 {@code null} 改成自己，改成功就说明拿到了锁；
 * 改失败说明锁已被其他线程持有，就在 {@code while} 里空转（自旋）重试。解锁则是把引用从自己 CAS 回 {@code null}。
 * <p>
 * 与 {@link AtomicReferenceDemo} 对比：同样是卖 10 张票，加了自旋锁后「判断 + 打印 + 减一」成为临界区，
 * 每张票只会被卖出一次，不会重复也不会把票减成负数。
 * <p>
 * 自旋锁的代价：等锁期间一直占用 CPU 空转，不适合临界区耗时较长或竞争激烈的场景；
 * 它也不支持重入（同一线程再次 lock 会永远自旋下去），生产代码应优先用
 * {@link java.util.concurrent.locks.ReentrantLock}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see AtomicReferenceDemo
 */
public class AtomicReferenceDemo2 {

    private static int ticket = 10;

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 5 个任务在 3 个线程上靠自旋锁互斥地卖 10 张票
     */
    public static void demo() throws InterruptedException {
        // 重置票数，保证本方法可以重复调用（否则第二次调用时 ticket 已不大于 0，不会有任何输出）
        ticket = 10;

        SpinLock lock = new SpinLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread(lock));
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 基于 {@link AtomicReference} 实现的简单自旋锁
     */
    static class SpinLock {

        private AtomicReference<Thread> atomicReference = new AtomicReference<>();

        public void lock() {
            Thread current = Thread.currentThread();
            while (!atomicReference.compareAndSet(null, current)) {}
        }

        public void unlock() {
            Thread current = Thread.currentThread();
            atomicReference.compareAndSet(current, null);
        }

    }

    /**
     * 利用自旋锁 {@link SpinLock} 并发处理数据
     */
    static class MyThread implements Runnable {

        private SpinLock lock;

        public MyThread(SpinLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (ticket > 0) {
                lock.lock();
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                    ticket--;
                }
                lock.unlock();
            }
        }

    }

}
