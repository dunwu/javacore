package io.github.dunwu.javase.concurrent.chapter01;

/**
 * @author Zhang Peng
 */
@SuppressWarnings("all")
public class SynchronizedDemo implements Runnable {
    private int ticket = 20;

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            this.sale(); // 调用同步方法
        }
    }

    private synchronized void sale() { // 声明同步方法
        if (ticket > 0) { // 还有票
            try {
                Thread.sleep(100); // 加入延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "卖票：ticket = " + ticket--);
        }
    }

    public static void main(String args[]) {
        SynchronizedDemo demo = new SynchronizedDemo(); // 定义线程对象
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(demo, "线程-" + i);
            t.start();
        }
    }
}
