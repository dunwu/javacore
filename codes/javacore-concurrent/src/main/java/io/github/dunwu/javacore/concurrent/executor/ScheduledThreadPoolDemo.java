package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个线程池，可以安排命令在给定延迟后运行，或定期执行。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        schedule();
        scheduleAtFixedRate();
    }

    private static void schedule() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            }, 1, TimeUnit.SECONDS);
        }
        executorService.shutdown();
    }

    private static void scheduleAtFixedRate() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        executorService.shutdown();
    }

}
