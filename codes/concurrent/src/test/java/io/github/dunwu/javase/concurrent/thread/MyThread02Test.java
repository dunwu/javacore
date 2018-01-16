package io.github.dunwu.javase.concurrent.thread;

import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class MyThread02Test {
    /**
     * 正确启动线程方式。
     */
    @Test
    public void testStart() {
        MyThread02 mt1 = new MyThread02("线程A "); // 实例化对象
        MyThread02 mt2 = new MyThread02("线程B "); // 实例化对象
        Thread thread1 = new Thread(mt1); // 实例化 Thread 对象
        Thread thread2 = new Thread(mt2); // 实例化 Thread 对象
        thread1.start(); // 调用线程主体
        thread2.start(); // 调用线程主体
    }
}
