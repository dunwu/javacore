package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class ThreadSleepDemo implements Runnable {
    private String name;
    private int time;

    public ThreadSleepDemo(String name, int time) {
        this.name = name; // 设置线程名称
        this.time = time; // 设置休眠时间
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.time); // 休眠指定的时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + "线程，休眠" + this.time + "毫秒。");
    }

    public static void main(String[] args) {
        ThreadSleepDemo mt1 = new ThreadSleepDemo("线程A", 1000); // 定义线程对象，指定休眠时间
        ThreadSleepDemo mt2 = new ThreadSleepDemo("线程B", 2000); // 定义线程对象，指定休眠时间
        ThreadSleepDemo mt3 = new ThreadSleepDemo("线程C", 3000); // 定义线程对象，指定休眠时间
        new Thread(mt1).start(); // 启动线程
        new Thread(mt2).start(); // 启动线程
        new Thread(mt3).start(); // 启动线程
    }
};
