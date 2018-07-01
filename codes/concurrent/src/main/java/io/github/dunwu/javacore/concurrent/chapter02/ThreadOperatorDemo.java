package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * @author Zhang Peng
 */
public class ThreadOperatorDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500); // 线程休眠
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 运行，i = " + i);
        }
    }
}
