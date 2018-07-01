package io.github.dunwu.javacore.concurrent.sync;

class Thread03 implements Runnable {
    private int ticket = 5; // 假设一共有5张票

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.sale(); // 调用同步方法
        }
    }

    public synchronized void sale() { // 声明同步方法
        if (ticket > 0) { // 还有票
            try {
                Thread.sleep(300); // 加入延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("卖票：ticket = " + ticket--);
        }

    }
};

public class SyncDemo03 {
    public static void main(String[] args) {
        Thread03 mt = new Thread03(); // 定义线程对象
        Thread t1 = new Thread(mt); // 定义Thread对象
        Thread t2 = new Thread(mt); // 定义Thread对象
        Thread t3 = new Thread(mt); // 定义Thread对象
        t1.start();
        t2.start();
        t3.start();
    }
};
