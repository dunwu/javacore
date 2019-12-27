package io.github.dunwu.javacore.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 示例
 *
 * @author Zhang Peng
 * @since 2018/5/11
 */
public class ReentrantReadWriteLockDemo {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        new Thread(new MyThread()).start();
        new Thread(new MyThread()).start();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                lock.readLock().lock();
                try {
                    long start = System.currentTimeMillis();

                    while (System.currentTimeMillis() - start <= 1) {
                        System.out.println(Thread.currentThread().getName() + "正在进行读操作");
                    }
                    System.out.println(Thread.currentThread().getName() + "读操作完毕");
                } finally {
                    lock.readLock().unlock();
                }
            }
        }

    }

}
