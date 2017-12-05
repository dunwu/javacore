package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class ThreadDemo extends Thread {
    private int ticket = 5;

    public ThreadDemo(String name) {
        super(name);
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (this.ticket > 0) {
                System.out.println(this.getName() + " 卖票：ticket = " + ticket--);
            }
        }
    }
}
