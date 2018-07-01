package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zhang Peng
 * @date 2018/5/24
 */
public class AtomicIntegerDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 1000; i++) {
            executorService.submit((Runnable) () -> {
                System.out.println(Thread.currentThread().getName() + " count=" + count.get());
                count.incrementAndGet();
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }
}
