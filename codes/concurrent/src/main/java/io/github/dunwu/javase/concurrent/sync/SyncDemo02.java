package io.github.dunwu.javase.concurrent.sync;

class Thread02 implements Runnable {
    private int ticket = 5; // 假设一共有5张票

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            synchronized (this) { // 要对当前对象进行同步
                if (ticket > 0) { // 还有票
                    try {
                        Thread.sleep(300); // 加入延迟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("卖票：ticket = " + ticket--);
                }
            }
        }
    }
};

public class SyncDemo02 {
    public static void main(String[] args) {
        Thread02 mt = new Thread02(); // 定义线程对象
        Thread t1 = new Thread(mt); // 定义Thread对象
        Thread t2 = new Thread(mt); // 定义Thread对象
        Thread t3 = new Thread(mt); // 定义Thread对象
        t1.start();
        t2.start();
        t3.start();
    }
};
