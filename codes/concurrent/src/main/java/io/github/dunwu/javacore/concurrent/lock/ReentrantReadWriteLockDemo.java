package io.github.dunwu.javacore.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 示例
 * @author Zhang Peng
 * @date 2018/5/11
 */
public class ReentrantReadWriteLockDemo {

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(() -> demo.get(Thread.currentThread())).start();
        new Thread(() -> demo.get(Thread.currentThread())).start();
    }

    public synchronized void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作完毕");
        } finally {
            rwl.readLock().unlock();
        }
    }
}
