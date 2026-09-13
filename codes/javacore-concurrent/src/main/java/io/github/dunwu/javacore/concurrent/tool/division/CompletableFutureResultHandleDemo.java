package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 结果处理示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class CompletableFutureResultHandleDemo {

    public static void main(String[] args) {
        try {
            System.out.println("============= whenComplete =============");
            whenCompleteDemo();
        } catch (Exception e) {
            // ignore
        }

        try {
            System.out.println("============= exceptionally & handle =============");
            exceptionallyAndHandleDemo();
        } catch (Exception e) {
            // ignore
        }
    }

    static void whenCompleteDemo() {
        // 成功情况
        CompletableFuture<String> ok = CompletableFuture.supplyAsync(() -> "Hello");
        ok.whenComplete((res, e) -> {
            System.out.println("结果: " + res + ", 异常: " + e);
        }).join();
        // 【输出】结果: Hello, 异常: null

        // 异常情况
        CompletableFuture<String> fail = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("失败");
        });
        fail.whenComplete((res, e) -> {
            System.out.println("结果: " + res + ", 异常: " + e.getMessage());
        }).join();
        // 【输出】结果: null, 异常: java.lang.RuntimeException: 失败
    }

    static void exceptionallyAndHandleDemo() {
        // 成功情况
        CompletableFuture<String> ok = CompletableFuture.supplyAsync(() -> "Hello");
        String okResult = ok.exceptionally(e -> "失败")
                            .handle((res, e) -> {
                                return "结果: " + res + ", 异常: " + e;
                            }).join();
        System.out.println(okResult);
        // 【输出】结果: Hello, 异常: null

        // 失败情况
        CompletableFuture<String> fail = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("失败");
        });
        String failResult = fail.exceptionally(e -> "失败")
                                .handle((res, e) -> {
                                    return "结果: " + res + ", 异常: " + e;
                                }).join();
        System.out.println(failResult);
        // 【输出】结果: 失败, 异常: null
    }

}
