package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 示例 字面意思为信号量
 * <p>
 * Semaphore 可以控制同时访问的线程个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see java.util.concurrent.Semaphore
 * @since 2018/5/10
 */
public class SemaphoreDemo {

    private static final int THREAD_COUNT = 30;
    private static Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("save data");
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
    }

}
