package io.github.dunwu.javase.concurrent.thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Zhang Peng
 */
public class LockDemo {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock(); // 注意这个地方

    public static void main(String[] args) {
        final LockDemo demo = new LockDemo();

        new Thread() {
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            };
        }.start();

        new Thread() {
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            };
        }.start();
    }

    public void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 50; i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(thread.getName() + "释放了锁");
            lock.unlock();
        }
    }
}
