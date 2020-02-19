package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 */
public class AtomicIntegerDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }

}
