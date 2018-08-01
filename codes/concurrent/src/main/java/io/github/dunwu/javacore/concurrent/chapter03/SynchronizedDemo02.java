package io.github.dunwu.javacore.concurrent.chapter03;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * @author Zhang Peng
 * @date 2018/8/1
 */
@ThreadSafe
public class SynchronizedDemo02 implements Runnable {
    static int i = 0;

    public synchronized void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo02 instance = new SynchronizedDemo02();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
// 输出结果:
// 2000000
