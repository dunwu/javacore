package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 示例 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see CyclicBarrier
 * @since 2018/5/10
 */
public class CyclicBarrierDemo02 {

    public static void main(String[] args) {
        Runnable barrier1Action = new Runnable() {
            @Override
            public void run() {
                System.out.println("BarrierAction 1 executed ");
            }
        };
        Runnable barrier2Action = new Runnable() {
            @Override
            public void run() {
                System.out.println("BarrierAction 2 executed ");
            }
        };

        CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
        CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

        CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(barrier1, barrier2);

        CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(barrier1, barrier2);

        new Thread(barrierRunnable1).start();
        new Thread(barrierRunnable2).start();
    }

    static class CyclicBarrierRunnable implements Runnable {

        CyclicBarrier barrier1 = null;

        CyclicBarrier barrier2 = null;

        CyclicBarrierRunnable(CyclicBarrier barrier1, CyclicBarrier barrier2) {
            this.barrier1 = barrier1;
            this.barrier2 = barrier2;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " waiting at barrier 1");
                this.barrier1.await();

                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " waiting at barrier 2");
                this.barrier2.await();

                System.out.println(Thread.currentThread().getName() + " done!");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

}
