package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 示例
 * 字面意思为信号量，Semaphore 可以控同时访问的线程个数，通过 acquire() 获取一个许可，
 * 如果没有就等待，而 release() 释放一个许可。
 * @author Zhang Peng
 * @date 2018/5/10
 * @see java.util.concurrent.Semaphore
 */
public class SemaphoreDemo {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(() -> {
                try {
                    s.acquire();
                    System.out.println("save data");
                    s.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadPool.shutdown();
    }
}
