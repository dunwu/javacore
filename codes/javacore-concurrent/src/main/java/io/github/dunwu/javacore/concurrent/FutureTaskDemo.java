package io.github.dunwu.javacore.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FutureTaskDemo {

    public static void main(String[] args) {
        // 第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();
        CallableDemo task = new CallableDemo();
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        executor.submit(futureTask);
        executor.shutdown();

        // 第二种方式，注意这种方式和第一种方式效果是类似的，只不过一个使用的是ExecutorService，一个使用的是Thread
        /*
         * Task task = new Task(); FutureTask<Integer> futureTask = new
         * FutureTask<Integer>(task); Thread thread = new Thread(futureTask);
         * thread.start();
         */

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果" + futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }

}
