package io.github.dunwu.javacore.jdk8.concurrent;

import java.util.concurrent.*;

/**
 * @author Benjamin Winterberg
 */
public class Executors2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        // test1();
        // test2();
        test3();
    }

    private static void test3() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        future.get(1, TimeUnit.SECONDS);
    }

    private static void test2() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        executor.shutdownNow();
        future.get();
    }

    private static void test1() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        System.out.println("future done: " + future.isDone());

        Integer result = future.get();

        System.out.println("future done: " + future.isDone());
        System.out.print("result: " + result);

        executor.shutdownNow();
    }

}
