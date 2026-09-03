package io.github.dunwu.javacore.concurrent.tool.sync;

import java.util.concurrent.CountDownLatch;

/**
 * 用两个 {@link CountDownLatch} 实现「起跑门 + 终点门」，精确测量 N 个线程并发执行任务的耗时
 * <p>
 * 这是并发基准测试的经典写法，也是 {@code CountDownLatch} 比 {@code Thread.join()} 更灵活的地方：
 * <ul>
 *     <li><b>startGate</b>（计数 1）：N 个线程创建后先全部阻塞在 {@code startGate.await()} 上，
 *     主线程 {@code countDown()} 一声令下，所有线程同时起跑。
 *     若不用起跑门，先创建的线程已经跑了很久后创建的线程才开始，测的就不是真正的并发耗时</li>
 *     <li><b>endGate</b>（计数 N）：每个线程在 {@code finally} 里 {@code countDown()}，
 *     主线程 {@code endGate.await()} 等到计数归零，即所有线程都跑完了，才停表。
 *     放在 {@code finally} 里是为了即使任务抛异常也能减计数，否则主线程会永远阻塞</li>
 * </ul>
 * 计时只包住「开闸 → 等全部结束」这一段，不包含创建和启动线程的开销，因此结果更准确。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/15
 */
public class CountDownLatchDemo02 {

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 3 个线程各打印 5 行，最后一行是本次并发执行的毫秒耗时。
     * 因此共 16 行：前 15 行的行顺序与线程名取决于调度，末行是一个不固定的耗时数字
     */
    public static void demo() throws InterruptedException {
        Runnable task = new MyThread();
        long time = timeTasks(3, task);
        System.out.println(time);
    }

    /**
     * 同时启动 {@code num} 个线程执行 {@code task}，返回从开闸到全部执行完毕的毫秒耗时
     */
    private static long timeTasks(int num, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(num);

        for (int i = 0; i < num; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                    }
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

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "运行，i = " + i);
            }
        }

    }

}
