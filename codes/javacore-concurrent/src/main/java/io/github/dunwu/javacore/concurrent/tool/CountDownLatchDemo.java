package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 示例
 * <p>
 * 作用：允许一个或多个线程等待，直到在其他线程中执行的一组操作完成。
 * <p>
 * 原理：CountDownLatch 维护一个计数器 count。每次调用 countDown 方法会让 count 的值减 1，减到 0 的时候，那些因为调用 await 方法而在等待的线程就会被唤醒。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see java.util.concurrent.CountDownLatch
 * @since 2018/5/10
 */
@SuppressWarnings("all")
public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(new MyThread(latch)).start();
        new Thread(new MyThread(latch)).start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyThread implements Runnable {

        private CountDownLatch latch;

        public MyThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
            latch.countDown();
        }

    }

}
