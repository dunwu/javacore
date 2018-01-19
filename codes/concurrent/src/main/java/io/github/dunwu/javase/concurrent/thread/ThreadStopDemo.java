package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class ThreadStopDemo {
    public static void main(String[] args) {
        ThreadStop my = new ThreadStop();
        Thread t = new Thread(my, "线程"); // 建立线程对象
        t.start(); // 启动线程
        try {
            Thread.sleep(30);
        } catch (Exception e) {

        }
        my.stop(); // 修改标志位，停止运行
    }
};

class ThreadStop implements Runnable {
    private boolean flag = true; // 定义标志位

    @Override
    public void run() {
        int i = 0;
        while (this.flag) {
            System.out.println(Thread.currentThread().getName() + "运行，i = " + (i++));
        }
    }

    public void stop() {
        this.flag = false; // 修改标志位
    }
};
