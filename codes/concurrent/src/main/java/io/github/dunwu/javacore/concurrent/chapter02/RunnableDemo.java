package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * 实现 Runnable 接口
 * @author Zhang Peng
 */
public class RunnableDemo {

    public static void main(String[] args) {
        MyThread t = new MyThread("Runnable 线程"); // 实例化对象
        new Thread(t).run(); // 调用线程主体
        new Thread(t).run(); // 调用线程主体
        new Thread(t).run(); // 调用线程主体
    }

    static class MyThread implements Runnable {

        private int ticket = 5;
        private String name;

        MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (this.ticket > 0) {
                    System.out.println(this.name + " 卖票：ticket = " + ticket--);
                }
            }
        }
    }
}
