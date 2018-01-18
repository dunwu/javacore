package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class ThreadYieldDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i);
            if (i == 2) {
                System.out.print("线程礼让：");
                Thread.currentThread().yield();
            }
        }
    }

    public static void main(String[] args) {
        ThreadYieldDemo thread = new ThreadYieldDemo();
        Thread t1 = new Thread(thread, "线程A");
        Thread t2 = new Thread(thread, "线程B");
        t1.start();
        t2.start();
    }
};
