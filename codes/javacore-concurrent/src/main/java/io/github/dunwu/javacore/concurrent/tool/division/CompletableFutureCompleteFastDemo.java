package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 完成控制示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class CompletableFutureCompleteFastDemo {

    public static void main(String[] args) {
        // 未完成就直接结束
        boolean isOver = true;
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "成功");
        if (isOver) {
            future.complete("结束");
        } else {
            future.cancel(false);
        }
        System.out.println("结果：" + future.join());
        // 【输出】结果：结束

        // 正常结束
        isOver = false;
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "成功");
        if (isOver) {
            future2.complete("结束");
        } else {
            future2.cancel(false);
        }
        System.out.println("结果：" + future2.join());
        // 【输出】结果：成功
    }

}
