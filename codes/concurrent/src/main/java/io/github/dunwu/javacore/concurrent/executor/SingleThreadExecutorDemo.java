package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个执行器，该执行器使用单例线程队列。 （但是请注意，如果这个单线程在关闭之前的执行期间由于失败而终止，那么如果需要执行后续任务，则新的线程将取代它。） 保证任务顺序执行，并且任意时间都不会出一个以上的任务被激活。 与其他等效的
 * newFixedThreadPool（1）不同，保证返回的执行程序不会被重新配置为使用其他线程。
 * @author Zhang Peng
 */
public class SingleThreadExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
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
