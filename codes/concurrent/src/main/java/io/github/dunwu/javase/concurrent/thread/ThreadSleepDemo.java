package io.github.dunwu.javase.concurrent.thread;

/**
 * Thread.sleep() 示例
 * sleep() 方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入 Blocked 状态。
 * 该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。
 * 但是，sleep() 方法不会释放“锁标志”，也就是说如果有 synchronized 同步块，其他线程仍然不能访问共享数据。
 * @author Zhang Peng
 * @date 2018/1/18
 * @see ThreadJoinDemo
 * @see ThreadYieldDemo
 */
public class ThreadSleepDemo implements Runnable {
    private String name;
    private int time;

    private ThreadSleepDemo(String name, int time) {
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
