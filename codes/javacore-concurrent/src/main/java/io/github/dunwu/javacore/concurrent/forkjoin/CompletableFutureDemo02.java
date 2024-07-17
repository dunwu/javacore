package io.github.dunwu.javacore.concurrent.forkjoin;

import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-02-21
 */
public class CompletableFutureDemo02 {

    public static void main(String[] args) {
        noException();
        catchException();
        catchException2();
    }

    public static void noException() {
        try {
            CompletableFuture.runAsync(() -> {
                int num = 1 / 0;
            });
        } catch (Exception e) {
            System.err.println("noException: " + e.getMessage());
        }
    }

    public static void catchException() {
        try {
            CompletableFuture.runAsync(() -> {
                int num = 1 / 0;
            }).get();
        } catch (Exception e) {
            System.err.println("catchException: " + e.getMessage());
        }
    }

    public static void catchException2() {
        try {
            CompletableFuture.runAsync(() -> {
                int num = 1 / 0;
            }).join();
        } catch (Exception e) {
            System.err.println("catchException2: " + e.getMessage());
        }
    }

}
