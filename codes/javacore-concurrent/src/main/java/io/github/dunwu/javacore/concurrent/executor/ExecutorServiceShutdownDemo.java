package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorService 的正确关闭方法
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-02-10
 */
public class ExecutorServiceShutdownDemo {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(5);
        final long waitTime = 8 * 1000;
        final long awaitTime = 5 * 1000;

        Runnable task1 = () -> {
            try {
                System.out.println("任务一开始");
                Thread.sleep(waitTime);
                System.out.println("任务一结束");
            } catch (InterruptedException e) {
                System.out.println("任务一中断: " + e);
            }
        };

        Runnable task2 = () -> {
            try {
                System.out.println("任务二开始");
                Thread.sleep(1000);
                System.out.println("任务二结束");
            } catch (InterruptedException e) {
                System.out.println("任务二中断: " + e);
            }
        };
        // 让学生解答某个很难的问题
        pool.execute(task1);

        // 让学生解答很多问题
        for (int i = 0; i < 1000; ++i) {
            pool.execute(task2);
        }

        try {
            // 向学生传达“问题解答完毕后请举手示意！”
            pool.shutdown();

            // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
            // (所有的任务都结束的时候，返回TRUE)
            if (!pool.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            System.out.println("awaitTermination interrupted: " + e);
            pool.shutdownNow();
        }

        System.out.println("====================== 全部结束 ======================");
    }

}
