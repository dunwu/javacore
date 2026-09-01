package io.github.dunwu.javacore.jdk9.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Java 9 CompletableFuture 增强示例。
 * <p>
 * Java 9 为 {@link CompletableFuture} 新增了三个实用方法：
 * <ul>
 * <li>{@code orTimeout(long, TimeUnit)}：超时后以 {@link TimeoutException} 异常完成</li>
 * <li>{@code completeOnTimeout(T, long, TimeUnit)}：超时后以给定默认值正常完成</li>
 * <li>{@code copy()}：创建一个副本，副本随原 Future 完成而完成（只读视图，可用于隔离取消操作）</li>
 * </ul>
 */
public class CompletableFutureEnhanceDemo {

    /**
     * 示例 1：orTimeout——任务超时未完成时，以 TimeoutException 异常完成
     */
    public static void orTimeoutDemo() {
        CompletableFuture<String> timeoutFuture = new CompletableFuture<>();
        timeoutFuture.orTimeout(100, TimeUnit.MILLISECONDS);
        try {
            timeoutFuture.join();
        } catch (CompletionException e) {
            System.out.println("orTimeout 超时，异常类型: " + e.getCause().getClass().getSimpleName());
        }
    }

    /**
     * 示例 2：completeOnTimeout——任务超时未完成时，以默认值正常完成
     */
    public static void completeOnTimeoutDemo() {
        CompletableFuture<String> fallbackFuture = new CompletableFuture<>();
        String result = fallbackFuture
            .completeOnTimeout("默认结果", 100, TimeUnit.MILLISECONDS)
            .join();
        System.out.println("completeOnTimeout 结果: " + result);
    }

    /**
     * 示例 3：未超时的场景——任务及时完成时，completeOnTimeout 不会生效
     */
    public static void noTimeoutScenario() {
        String fast = CompletableFuture.completedFuture("及时完成")
            .completeOnTimeout("默认结果", 1, TimeUnit.SECONDS)
            .join();
        System.out.println("及时完成场景结果: " + fast);
    }

    /**
     * 示例 4：copy——副本随原 Future 完成而完成
     */
    public static void copyDemo() {
        CompletableFuture<String> origin = new CompletableFuture<>();
        CompletableFuture<String> copied = origin.copy();
        origin.complete("原始任务完成");
        System.out.println("copy 副本结果: " + copied.join());
    }

    public static void main(String[] args) {
        orTimeoutDemo();
        completeOnTimeoutDemo();
        noTimeoutScenario();
        copyDemo();
    }

}
// Output:
// orTimeout 超时，异常类型: TimeoutException
// completeOnTimeout 结果: 默认结果
// 及时完成场景结果: 及时完成
// copy 副本结果: 原始任务完成
