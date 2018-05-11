package io.github.dunwu.javase.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Peng
 * @date 2018/5/11
 */
public class ThreadWaitNotifyDemo {

    public static void main(String[] args) {
        final Object lock = new Object();

        new Thread(() -> {
            System.out.println("thread A is waiting to get lock");
            synchronized (lock) {
                try {
                    System.out.println("thread A get lock");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("thread A do wait method");
                    lock.wait();
                    System.out.println("wait end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            System.out.println("thread B is waiting to get lock");
            synchronized (lock) {
                System.out.println("thread B get lock");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.notify();
                System.out.println("thread B do notify method");
            }
        }).start();
    }
}
