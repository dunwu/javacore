package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.CountDownLatch;

/**
 * @author Zhang Peng
 * @date 2018/5/15
 */
public class CountDownLatchDemo02 {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = new ThreadName();
        long time = timeTasks(3, task);
        System.out.println(time);
    }

    private static long timeTasks(int num, final Runnable task)
        throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(num);

        for (int i = 0; i < num; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException ignored) {
                }
            });
            t.start();
        }

        long start = System.currentTimeMillis();
        startGate.countDown();
        endGate.await();
        long end = System.currentTimeMillis();
        return end - start;
    }

    static class ThreadName implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "运行，i = " + i);
            }
        }
    }

    ;
}
