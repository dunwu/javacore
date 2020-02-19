package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 利用 {@link AtomicReference} 实现自旋锁
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class AtomicReferenceDemo2 {

    private static int ticket = 10;

    public static void main(String[] args) {
        threadSafeDemo();
    }

    private static void threadSafeDemo() {
        SpinLock lock = new SpinLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread(lock));
        }
        executorService.shutdown();
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
