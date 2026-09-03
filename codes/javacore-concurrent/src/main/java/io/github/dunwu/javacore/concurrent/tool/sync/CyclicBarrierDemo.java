package io.github.dunwu.javacore.concurrent.tool.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 示例：让一组线程互相等待，全部到达屏障后再一起继续
 * <p>
 * 字面意思是「回环栅栏」：创建时指定参与方个数 N，每个线程调 {@code await()} 后就阻塞，
 * 直到第 N 个线程也到达，所有线程才被同时释放。「回环」指的是栅栏可重用：释放后计数自动重置，
 * 下一轮可以继续用（这一点与只能使用一次的 {@link java.util.concurrent.CountDownLatch} 不同）。
 * <p>
 * 构造时传入的 {@code barrierAction} 会在<b>最后一个到达的线程</b>上、且在所有线程被释放<b>之前</b>执行一次，
 * 适合用来做「汇总」工作。因此它的输出必然是本例的最后一行。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see java.util.concurrent.CyclicBarrier
 * @see CountDownLatchDemo
 * @since 2018/5/10
 */
public class CyclicBarrierDemo {

    final static int N = 4;

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 4 个线程各自 sleep 3 秒模拟写数据，然后到栅栏上相互等待。
     * 输出共 9 行：4 行「正在写入数据...」、4 行「写入数据完毕，等待其他线程写入完毕」，
     * 最后一行是栅栏动作打印的「当前线程Thread-N」。前 8 行的顺序与线程名取决于调度，
     * 但栅栏动作必然在最后。末尾的 {@code join} 保证方法返回前所有输出已打印完毕
     */
    public static void demo() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(N,
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程" + Thread.currentThread().getName());
                }
            });

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(new MyThread(barrier));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    static class MyThread implements Runnable {

        private CyclicBarrier cyclicBarrier;

        MyThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
            try {
                Thread.sleep(3000); // 以睡眠来模拟写入数据操作
                System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

}
