package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFuture 创建
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2024-02-21
 */
public class CompletableFutureCreateDemo {

    public static void main(String[] args) {
        // 无返回值的异步任务
        CompletableFuture.runAsync(() -> {
            System.out.println("无返回值的异步任务");
        }).join();

        // 有返回值的异步任务
        String res = CompletableFuture.supplyAsync(() -> "有返回值的异步任务").join();
        System.out.println(res);

        // 指定自定义线程池
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String res2 = CompletableFuture.supplyAsync(() -> "自定义线程池且有返回值的异步任务", executor).join();
        System.out.println(res2);
        executor.shutdown();
    }

}
