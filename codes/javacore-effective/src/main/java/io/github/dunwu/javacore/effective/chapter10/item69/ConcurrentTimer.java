// Simple framework for timing concurrent execution
package io.github.dunwu.javacore.effective.chapter10.item69;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class ConcurrentTimer {

    private ConcurrentTimer() {
    } // Noninstantiable

    public static long time(Executor executor, int concurrency, final Runnable action) throws InterruptedException {
        final CountDownLatch ready = new CountDownLatch(concurrency);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ready.countDown(); // Tell timer we're ready
                    try {
                        start.await(); // Wait till peers are ready
                        action.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown(); // Tell timer we're done
                    }
                }
            });
        }

        ready.await(); // Wait for all workers to be ready
        long startNanos = System.nanoTime();
        start.countDown(); // And they're off!
        done.await(); // Wait for all workers to finish
        return System.nanoTime() - startNanos;
    }

}
