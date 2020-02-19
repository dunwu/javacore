package io.github.dunwu.javacore.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FutureDemo {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CallableDemo task = new CallableDemo();
        Future<Integer> result = executor.submit(task);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果" + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }

}
