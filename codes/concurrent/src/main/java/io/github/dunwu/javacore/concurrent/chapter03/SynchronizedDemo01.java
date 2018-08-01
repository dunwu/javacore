package io.github.dunwu.javacore.concurrent.chapter03;

import io.github.dunwu.javacore.concurrent.annotation.NotThreadSafe;

/**
 * @author Zhang Peng
 * @date 2018/8/1
 */
@NotThreadSafe
public class SynchronizedDemo01 implements Runnable {
    static int i = 0;

    public void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo01 instance = new SynchronizedDemo01();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
// 输出结果: 小于 200000 的随机数字
