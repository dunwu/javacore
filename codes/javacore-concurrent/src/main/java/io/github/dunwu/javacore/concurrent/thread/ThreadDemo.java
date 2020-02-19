package io.github.dunwu.javacore.concurrent.thread;

/**
 * 继承 {@link Thread} 类方式创建线程。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ThreadDemo {

    public static void main(String[] args) {
        // 实例化对象
        MyThread tA = new MyThread("Thread 线程-A");
        MyThread tB = new MyThread("Thread 线程-B");
        // 调用线程主体
        tA.start();
        tB.start();
    }

    static class MyThread extends Thread {

        private int ticket = 5;

        MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
