package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个线程池，根据需要创建新线程，但在可用时将重用先前构建的线程。
 * 这些池通常会提高执行许多短暂异步任务的程序的性能。
 * 调用执行将重用以前构造的线程（如果可用）。
 * 如果没有现有的线程可用，则会创建一个新的线程并将其添加到池中。
 * 未使用六十秒的线程被终止并从缓存中移除。因此，闲置时间足够长的池不会消耗任何资源。
 * 请注意，可能使用ThreadPoolExecutor构造函数创建具有相似属性但具有不同细节的池（例如，超时参数）。
 * @author Zhang Peng
 */
public class CachedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.execute(() -> System.out.println(Thread.currentThread().getName() + " 执行，i = " + index));
        }
    }
}
