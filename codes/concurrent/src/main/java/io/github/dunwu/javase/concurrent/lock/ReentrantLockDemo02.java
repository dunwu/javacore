package io.github.dunwu.javase.concurrent.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock tryLock 示例
 * @author Zhang Peng
 * @date 2018/5/11
 */
@SuppressWarnings("all")
public class ReentrantLockDemo02 {

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final ReentrantLockDemo02 demo = new ReentrantLockDemo02();
        new Thread(() -> demo.insert(Thread.currentThread())).start();
        new Thread(() -> demo.insert(Thread.currentThread())).start();
    }

    public void insert(Thread thread) {
        if (lock.tryLock()) {
            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName() + "获取锁失败");
        }
    }
}
