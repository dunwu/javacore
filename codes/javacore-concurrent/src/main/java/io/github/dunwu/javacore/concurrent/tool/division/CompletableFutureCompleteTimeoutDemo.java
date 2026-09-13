package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 完成控制示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class CompletableFutureCompleteTimeoutDemo {

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
                return "任务完成";
            } catch (InterruptedException e) {
                return "任务取消";
            }
        });

        Thread.sleep(2000);
        future.cancel(true);
        System.out.println("任务状态: " + future.isCancelled());
        // 【输出】任务状态: true
    }

}
