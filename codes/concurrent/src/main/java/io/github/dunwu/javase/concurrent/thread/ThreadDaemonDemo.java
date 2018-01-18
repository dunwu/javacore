package io.github.dunwu.javase.concurrent.thread;

class ThreadDaemon implements Runnable { // 实现Runnable接口
    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + "在运行。");
        }
    }
};




public class ThreadDaemonDemo {
    public static void main(String args[]) {
        ThreadDaemon mt = new ThreadDaemon(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.setDaemon(true); // 此线程在后台运行
        t.start(); // 启动线程
    }
};
