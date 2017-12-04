package io.github.dunwu.javase.concurrent.chapter01;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class ThreadDemoTest extends Thread {
    @Test
    public void test() {
        ThreadDemo threadDemo1 = new ThreadDemo("线程A"); // 实例化对象
        ThreadDemo threadDemo2 = new ThreadDemo("线程B"); // 实例化对象
        ThreadDemo threadDemo3 = new ThreadDemo("线程C"); // 实例化对象
        threadDemo1.run(); // 调用线程主体
        threadDemo2.run(); // 调用线程主体
        threadDemo3.run(); // 调用线程主体
    }

    @Test
    public void testCurrentThread() {
        MyThread target = new MyThread(); // 实例化Runnable子类对象
        new Thread(target, "线程").start(); // 启动线程
        target.run(); // 直接调用run()方法
    }

    @Test
    public void testSetDaemon() {
        MyThread target = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(target, "线程"); // 实例化Thread对象
        t.setDaemon(true); // 此线程在后台运行
        t.start(); // 启动线程
    }

    @Test
    public void testThreadName() {
        MyThread target = new MyThread(); // 实例化Runnable子类对象
        new Thread(target).start(); // 系统自动设置线程名称
        new Thread(target, "线程-A").start(); // 手工设置线程名称
        new Thread(target, "线程-B").start(); // 手工设置线程名称
        new Thread(target).start(); // 系统自动设置线程名称
        new Thread(target).start(); // 系统自动设置线程名称
    }

    @Test
    public void testThreadAlive() {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        System.out.println("线程开始执行之前 --> " + t.isAlive()); // 判断是否启动
        Assert.assertFalse(t.isAlive());
        t.start(); // 启动线程
        System.out.println("线程开始执行之后 --> " + t.isAlive()); // 判断是否启动
        Assert.assertTrue(t.isAlive());
        for (int i = 0; i < 3; i++) {
            System.out.println(" main运行 --> " + i);
        }
        // 以下的输出结果不确定
        System.out.println("代码执行之后 --> " + t.isAlive()); // 判断是否启动
    }

    @Test
    public void testThreadPriority() {
        Thread t1 = new Thread(new MyThread(), "线程A"); // 实例化线程对象
        Thread t2 = new Thread(new MyThread(), "线程B"); // 实例化线程对象
        Thread t3 = new Thread(new MyThread(), "线程C"); // 实例化线程对象
        t1.setPriority(Thread.MIN_PRIORITY); // 优先级最低
        t2.setPriority(Thread.MAX_PRIORITY); // 优先级最低
        t3.setPriority(Thread.NORM_PRIORITY); // 优先级最低
        t1.start(); // 启动线程
        t2.start(); // 启动线程
        t3.start(); // 启动线程
    }

    @Test
    public void testThreadSleep() {
        MyThread target = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(target, "线程"); // 实例化Thread对象
        t.start(); // 启动线程

    }
}
