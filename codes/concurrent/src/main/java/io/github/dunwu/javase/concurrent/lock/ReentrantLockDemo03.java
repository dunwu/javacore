package io.github.dunwu.javase.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock lockInterruptibly() 示例
 * @author Zhang Peng
 * @date 2018/5/11
 */
@SuppressWarnings("all")
public class ReentrantLockDemo03 {

    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockDemo03 demo = new ReentrantLockDemo03();
        MyThread thread1 = new MyThread(demo);
        MyThread thread2 = new MyThread(demo);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
    }

    public void insert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();   //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {
            System.out.println(thread.getName() + "得到了锁");
            long startTime = System.currentTimeMillis();
            for (; ; ) {
                if (System.currentTimeMillis() - startTime >= Integer.MAX_VALUE) { break; }
                //插入数据
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + "执行finally");
            lock.unlock();
            System.out.println(thread.getName() + "释放了锁");
        }
    }

    static class MyThread extends Thread {

        private ReentrantLockDemo03 demo = null;

        public MyThread(ReentrantLockDemo03 test) {
            this.demo = test;
        }

        @Override
        public void run() {
            try {
                demo.insert(Thread.currentThread());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "被中断");
            }
        }
    }

}
