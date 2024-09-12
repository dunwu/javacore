package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask 交给线程池执行
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建FutureTask
        Task task = new Task();
        FutureTask<String> f1 = new FutureTask<>(task);
        FutureTask<String> f2 = new FutureTask<>(task);

        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(f1);
        executor.submit(f2);
        System.out.println(f1.get());
        System.out.println(f2.get());
        executor.shutdown();
    }

    static class Task implements Callable<String> {

        @Override
        public String call() {
            return Thread.currentThread().getName() + " 执行成功！";
        }

    }

}
// 输出
// pool-1-thread-1 执行成功！
// pool-1-thread-2 执行成功！
