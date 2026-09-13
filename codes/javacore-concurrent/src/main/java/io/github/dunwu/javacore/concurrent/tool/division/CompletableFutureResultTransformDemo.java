package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 结果z示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2025-10-11
 */
public class CompletableFutureResultTransformDemo {

    public static void main(String[] args) {
        try {
            System.out.println("============= 链式调用 =============");
            chainCallDemo();
        } catch (Exception e) {
            // ignore
        }
    }

    static void chainCallDemo() {
        // 链式调用，模拟下单流程
        String res = CompletableFuture.supplyAsync(() -> {
            System.out.println("1. 创建订单");
            return "订单 ID:1";
        }).thenApplyAsync(orderId -> {
            System.out.println("2. 扣减库存 " + orderId);
            return orderId + " 库存已扣";
        }).thenApplyAsync(result -> {
            System.out.println("3. 生成物流单 " + result);
            return result + " 物流单已生成";
        }).thenApplyAsync(result -> {
            System.out.println("4. 发送通知 " + result);
            return result + " 通知已发送";
        }).join();
        System.out.println("最终结果: " + res);
        // 【输出】
        // 1. 创建订单
        // 2. 扣减库存 订单 ID:1
        // 3. 生成物流单 订单 ID:1 库存已扣
        // 4. 发送通知 订单 ID:1 库存已扣 物流单已生成
        // 最终结果: 订单 ID:1 库存已扣 物流单已生成 通知已发送
    }

}
