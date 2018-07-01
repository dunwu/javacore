package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * Thread stop 方法示例 stop 方法有缺陷，已废弃
 * @author Zhang Peng
 */
public class ThreadStopDemo02 {

    public static void main(String[] args) {
        MyThread my = new MyThread();
        new Thread(my, "线程").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        my.stop(); // 修改标志位，停止运行
    }

    static class MyThread implements Runnable {

        private volatile boolean flag = true; // 定义标志位

        @Override
        public void run() {
            int i = 0;
            while (this.flag) {
                System.out.println(Thread.currentThread().getName() + "运行，i = " + (i++));
            }
            System.out.println(Thread.currentThread().getName() + "线程停止");
        }

        void stop() {
            this.flag = false; // 修改标志位
        }
    }
}


