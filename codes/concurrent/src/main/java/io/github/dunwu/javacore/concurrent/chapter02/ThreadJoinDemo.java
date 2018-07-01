package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * thread.join() 示例 join() 方法会使当前线程等待调用 join() 方法的线程结束后才能继续执行。
 * @author Zhang Peng
 * @date 2018/1/18
 * @see ThreadSleepDemo
 * @see ThreadYieldDemo
 */
public class ThreadJoinDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "mythread"); // 实例化Thread对象
        t.start(); // 启动线程
        for (int i = 0; i < 50; i++) {
            if (i > 10) {
                try {
                    t.join(); // 线程强制运行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Main 线程运行 --> " + i);
        }
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " 运行，i = " + i); // 取得当前线程的名字
            }
        }
    }
}
