package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask 交给线程执行
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FutureTaskDemo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 创建FutureTask
        Task task = new Task();
        FutureTask<String> f1 = new FutureTask<>(task);
        FutureTask<String> f2 = new FutureTask<>(task);

        // 创建线程
        new Thread(f1).start();
        new Thread(f2).start();
        System.out.println(f1.get());
        System.out.println(f2.get());
    }

    static class Task implements Callable<String> {

        @Override
        public String call() {
            return Thread.currentThread().getName() + " 执行成功！";
        }

    }

}
// 输出
// Thread-0 执行成功！
// Thread-1 执行成功！
