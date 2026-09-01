package io.github.dunwu.javacore.jdk8.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Java 8 CompletableFuture 异步编程示例。
 * <p>
 * {@link CompletableFuture} 实现了 {@code Future} + {@code CompletionStage}，
 * 解决了传统 Future "只能阻塞 get、无法回调、无法编排"的痛点：
 * <ul>
 * <li>{@code runAsync / supplyAsync}：开启异步任务</li>
 * <li>{@code thenApply / thenAccept / thenRun / thenCompose}：串行编排</li>
 * <li>{@code thenCombine / allOf / anyOf}：并行编排与合并</li>
 * <li>{@code exceptionally / handle}：异常处理</li>
 * </ul>
 */
public class CompletableFutureDemo {

    /**
     * 示例 1：创建异步任务，supplyAsync 有返回值，runAsync 无返回值
     */
    public static void asyncCreation() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "Java";
        });
        System.out.println("supplyAsync 结果: " + future.get());
    }

    /**
     * 示例 2：串行编排，thenApply 转换结果，thenAccept 消费结果
     */
    public static void serialComposition() throws ExecutionException, InterruptedException {
        CompletableFuture<String> chained = CompletableFuture.supplyAsync(() -> "Java")
            .thenApply(s -> s + " 8")
            .thenApply(String::toUpperCase);
        System.out.println("thenApply 链式转换: " + chained.get());
        CompletableFuture.supplyAsync(() -> "hello")
            .thenAccept(s -> System.out.println("thenAccept 消费: " + s))
            .get(); // get 保证异步动作执行完
    }

    /**
     * 示例 3：并行编排，两个独立任务并行执行后 thenCombine 合并（总耗时约 50ms 而非 100ms）
     */
    public static void parallelComposition() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> priceTask = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return 100;
        });
        CompletableFuture<Double> discountTask = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return 0.8;
        });
        double finalPrice = priceTask.thenCombine(discountTask, (price, discount) -> price * discount).get();
        System.out.println("thenCombine 折后价: " + finalPrice);
    }

    /**
     * 示例 4：allOf 等待全部任务完成
     */
    public static void waitAll() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> all = CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> sleep(10)),
            CompletableFuture.runAsync(() -> sleep(20)));
        all.get();
        System.out.println("allOf: 全部任务完成");
    }

    /**
     * 示例 5：异常处理，exceptionally 兜底，handle 同时处理结果和异常。
     * 注意：只抛异常的 lambda 无法推断返回类型，需先声明为 Supplier
     */
    public static void exceptionHandling() throws ExecutionException, InterruptedException {
        Supplier<String> failingTask = () -> {
            throw new RuntimeException("模拟失败");
        };
        String recovered = CompletableFuture.supplyAsync(failingTask)
            .exceptionally(e -> "异常兜底: " + e.getCause())
            .get();
        System.out.println(recovered);
        String handled = CompletableFuture.supplyAsync(failingTask)
            .handle((result, e) -> e != null ? "handle 捕获异常" : result)
            .get();
        System.out.println(handled);
    }

    /**
     * 示例 6：最佳实践，使用自定义线程池，避免占用公共 ForkJoinPool
     */
    public static void customExecutor() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            String inPool = CompletableFuture.supplyAsync(
                () -> "自定义线程池: " + Thread.currentThread().getName().contains("pool"), pool).get();
            System.out.println(inPool);
        } finally {
            pool.shutdown();
        }
    }
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        asyncCreation();
        serialComposition();
        parallelComposition();
        waitAll();
        exceptionHandling();
        customExecutor();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
// Output:
// supplyAsync 结果: Java
// thenApply 链式转换: JAVA 8
// thenAccept 消费: hello
// thenCombine 折后价: 80.0
// allOf: 全部任务完成
// 异常兜底: java.lang.RuntimeException: 模拟失败
// handle 捕获异常
// 自定义线程池: true
