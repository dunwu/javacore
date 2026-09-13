package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolFibonacciDemo {

    public static void main(String[] args) {
        long number = 10;
        ForkJoinPool pool = new ForkJoinPool();
        FibonacciTask task = new FibonacciTask(number);
        long result = pool.invoke(task);
        System.out.println("斐波那契数列第 " + number + " 项: " + result);
    }

    public static class FibonacciTask extends RecursiveTask<Long> {
        private final long N;

        public FibonacciTask(long n) {
            this.N = n;
        }

        @Override
        protected Long compute() {
            if (N <= 1) {
                return N;
            }

            // Fork：分解任务
            FibonacciTask f1 = new FibonacciTask(N - 1);
            FibonacciTask f2 = new FibonacciTask(N - 2);

            // 异步执行第一个任务
            f1.fork();

            // 同步执行第二个任务并获取结果
            long result2 = f2.compute();

            // Join：合并结果
            long result1 = f1.join();

            return result1 + result2;
        }
    }
}
