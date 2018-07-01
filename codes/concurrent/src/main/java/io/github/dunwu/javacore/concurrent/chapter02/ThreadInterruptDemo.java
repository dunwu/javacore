package io.github.dunwu.javacore.concurrent.chapter02;

public class ThreadInterruptDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.start(); // 启动线程
        try {
            Thread.sleep(2000); // 线程休眠2秒
        } catch (InterruptedException e) {
            System.out.println("3、休眠被终止");
        }
        t.interrupt(); // 中断线程执行
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println("1、进入run()方法");
            try {
                Thread.sleep(10000); // 线程休眠10秒
                System.out.println("2、已经完成了休眠");
            } catch (InterruptedException e) {
                System.out.println("3、休眠被终止");
                return; // 返回调用处
            }
            System.out.println("4、run()方法正常结束");
        }
    }
}
