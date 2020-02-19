package io.github.dunwu.javacore.concurrent.thread;

/**
 * get/set 线程名称示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ThreadNameDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        new Thread(mt).start(); // 系统自动设置线程名称
        new Thread(mt, "线程-A").start(); // 手工设置线程名称
        Thread t = new Thread(mt); // 手工设置线程名称
        t.setName("线程-B");
        t.start();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + "运行，i = " + i); // 取得当前线程的名字
            }
        }

    }

}
