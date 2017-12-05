package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class RunnableDemo implements Runnable {
    private int ticket = 5;
    private String name;

    public RunnableDemo() {

    }

    public RunnableDemo(String name) {
        this.name = name;
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (this.ticket > 0) {
                System.out.println(this.name + " 卖票：ticket = " + ticket--);
            }
        }
    }
}
