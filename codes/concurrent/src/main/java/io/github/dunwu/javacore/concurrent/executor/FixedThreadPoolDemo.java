package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 创建一个线程池，重用使用共享的固定数量的线程队列。 在任何时候，至多 nThreads 个线程将被主动处理任务。 如果在所有线程处于活动状态时提交其他任务，则会在队列中等待，直到线程可用。
 * 如果任何线程在关闭之前的执行期间由于失败而终止，那么如果需要执行后续任务，则将取代它。 池中的线程将一直存在，直到明确关闭。
 * @author Zhang Peng
 */
public class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 执行，i = " + index);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
