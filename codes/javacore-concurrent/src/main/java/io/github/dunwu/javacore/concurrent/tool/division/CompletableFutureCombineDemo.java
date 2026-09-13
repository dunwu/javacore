package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 组合示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class CompletableFutureCombineDemo {

    public static void main(String[] args) {
        System.out.println("============= 简单示例 =============");
        simpleDemo();
    }

    static void simpleDemo() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> 30);

        // 合并多个结果
        Integer res = future1.thenCombine(future2, (a, b) -> a + b)
                             .thenCombine(future3, (ab, c) -> ab + c)
                             .join();

        System.out.println("总和: " + res);
        // 【输出】总和: 60
    }

}

