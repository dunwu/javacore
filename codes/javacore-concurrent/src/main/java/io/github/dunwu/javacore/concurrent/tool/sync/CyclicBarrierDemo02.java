package io.github.dunwu.javacore.concurrent.tool.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 示例：两个线程分阶段同步，并演示栅栏动作的执行时机
 * <p>
 * 与 {@link CyclicBarrierDemo} 的区别在于这里用了<b>两道栅栏</b>，两个线程必须两轮都互相对齐：
 * 先一起到达 barrier1，再一起到达 barrier2，最后各自结束。
 * 这展示了 CyclicBarrier 适合「分阶段并行计算」的场景：每一阶段全部算完后才能进入下一阶段。
 * <p>
 * 两个 {@code barrierAction} 分别在<b>最后到达的那一个线程</b>上执行，且在所有线程被释放之前，
 * 因此输出里「BarrierAction 1 executed」必然夹在两组 waiting 行之间，位置是确定的。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see CyclicBarrier
 * @see CyclicBarrierDemo
 * @since 2018/5/10
 */
public class CyclicBarrierDemo02 {

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 输出共 8 行，行位置是确定的（仅线程名不固定）：
     * 第 1、2 行是两线程的「waiting at barrier 1」，第 3 行是「BarrierAction 1 executed」，
     * 第 4、5 行是「waiting at barrier 2」，第 6 行是「BarrierAction 2 executed」，
     * 第 7、8 行是两线程的「done!」。
     * 末尾的 {@code join} 保证方法返回前所有输出已打印完毕
     */
    public static void demo() throws InterruptedException {
        Runnable barrier1Action = new Runnable() {
            @Override
            public void run() {
                System.out.println("BarrierAction 1 executed");
            }
        };
        Runnable barrier2Action = new Runnable() {
            @Override
            public void run() {
                System.out.println("BarrierAction 2 executed");
            }
        };

        CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
        CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

        CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(barrier1, barrier2);

        CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(barrier1, barrier2);

        Thread t1 = new Thread(barrierRunnable1);
        Thread t2 = new Thread(barrierRunnable2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
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
