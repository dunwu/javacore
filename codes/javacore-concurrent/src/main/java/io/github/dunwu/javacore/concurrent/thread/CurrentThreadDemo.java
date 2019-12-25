package io.github.dunwu.javacore.concurrent.thread;

public class CurrentThreadDemo implements Runnable {

    public static void main(String[] args) {
        CurrentThreadDemo thread = new CurrentThreadDemo(); // 实例化Runnable子类对象
        new Thread(thread, "线程").start(); // 启动线程
        thread.run(); // 直接调用run()方法
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            // 取得当前线程的名字
            System.out.println(Thread.currentThread().getName() + " 运行，i = " + i);
        }
    }

}
