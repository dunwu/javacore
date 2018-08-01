package io.github.dunwu.javacore.concurrent.chapter03;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * @author Zhang Peng
 * @date 2018/8/1
 */
@ThreadSafe
public class SynchronizedDemo05 implements Runnable {
    static int i = 0;
    static SynchronizedDemo05 instance = new SynchronizedDemo05();

    @Override
    public void run() {
        synchronized (this) {
            for (int j = 0; j < 100000; j++) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
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
// 200000
