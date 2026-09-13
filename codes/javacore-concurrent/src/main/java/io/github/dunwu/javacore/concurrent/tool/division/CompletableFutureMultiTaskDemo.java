package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 多任务组合示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class CompletableFutureMultiTaskDemo {

    public static void main(String[] args) {
        System.out.println("============= 简单示例 =============");
        simpleDemo();

        System.out.println("============= 模拟多个微服务调用 =============");
        callMultiServicesDemo();

        System.out.println("============= 模拟批量数据处理 =============");
        batchOpsDemo();
    }

    static void simpleDemo() {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> "结果 1");
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> "结果 2");
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> "结果 3");

        // 所有任务完成后执行
        CompletableFuture<Void> all = CompletableFuture.allOf(task1, task2, task3);
        // 先等待所有完成
        all.thenRun(() -> {
            String res1 = task1.join();
            String res2 = task2.join();
            String res3 = task3.join();
            System.out.println("聚合结果：" + res1 + ", " + res2 + ", " + res3);
        }).join();
        // 【输出】聚合结果：结果 1, 结果 2, 结果 3

        CompletableFuture<Object> any = CompletableFuture.anyOf(task1, task2, task3);
        System.out.println("任意结果：" + any.join());
        // 【输出】任意结果：结果 1
    }

    static void callMultiServicesDemo() {
        // 模拟多个微服务调用
        CompletableFuture<String> userService = CompletableFuture.supplyAsync(() -> "用户信息");
        CompletableFuture<String> orderService = CompletableFuture.supplyAsync(() -> "订单信息");
        CompletableFuture<String> productService = CompletableFuture.supplyAsync(() -> "商品信息");

        // 并行执行，等待所有完成
        CompletableFuture<Void> all = CompletableFuture.allOf(userService, orderService, productService);
        all.thenRun(() -> {
            String user = userService.join();
            String order = orderService.join();
            String product = productService.join();
            System.out.println("聚合结果：" + user + ", " + order + ", " + product);
        }).join();
        // 【输出】聚合结果：用户信息, 订单信息, 商品信息
    }

    static void batchOpsDemo() {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);

        // 并行处理每个数据
        List<CompletableFuture<String>> futures =
            data.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> {
                    // 模拟数据处理
                    // System.out.println(Thread.currentThread().getName() + " 处理: " + item);
                    return "处理结果:" + item;
                }))
                .collect(Collectors.toList());

        // 等待所有处理完成
        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        List<String> results = allDone.thenApply(v ->
            futures.stream()
                   .map(CompletableFuture::join)
                   .collect(Collectors.toList())
        ).join();

        System.out.println("批量处理结果: " + results);
        // 【输出】批量处理结果: [处理结果:1, 处理结果:2, 处理结果:3, 处理结果:4, 处理结果:5]
    }

}

