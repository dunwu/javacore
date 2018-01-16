package io.github.dunwu.javase.concurrent.thread;

import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class MyThread01Test extends Thread {
    /**
     * 错误启动线程方式。使用 run() 方法启动线程并不会交错执行。
     */
    @Test
    public void testRun() {
        MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
        MyThread01 mt2 = new MyThread01("线程B "); // 实例化对象
        mt1.run(); // 调用线程主体
        mt2.run(); // 调用线程主体
    }

    /**
     * 正确启动线程方式。
     */
    @Test
    public void testStart() {
        MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
        MyThread01 mt2 = new MyThread01("线程B "); // 实例化对象
        mt1.start(); // 调用线程主体
        mt2.start(); // 调用线程主体
    }

    /**
     * 重复调用 start() 会抛出 java.lang.IllegalThreadStateException 异常。
     */
    @Test
    public void testStart02() {
        MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
        mt1.start(); // 调用线程主体
        mt1.start(); // 调用线程主体
    }
}
