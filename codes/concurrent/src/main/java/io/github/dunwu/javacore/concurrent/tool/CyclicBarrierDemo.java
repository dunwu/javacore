package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 示例
 * 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
 * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。
 * @author Zhang Peng
 * @date 2018/5/10
 * @see java.util.concurrent.CyclicBarrier
 */
public class CyclicBarrierDemo {

    final static int N = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(N,
            () -> System.out.println("当前线程" + Thread.currentThread().getName()));
        for (int i = 0; i < N; i++) { new MyThread(barrier).start(); }
    }

    private static class MyThread extends Thread {

        private CyclicBarrier cyclicBarrier;

        MyThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
