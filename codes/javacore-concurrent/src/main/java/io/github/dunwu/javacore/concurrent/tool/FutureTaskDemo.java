package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask 交给线程池执行
 * <p>
 * {@link FutureTask} 同时实现了 {@link Runnable} 和 {@link java.util.concurrent.Future}，
 * 因此既能提交给线程池执行，又能用 {@code get()} 阻塞地拿到 {@link Callable#call()} 的返回值。
 * <p>
 * 本例把<b>同一个</b> {@link Task} 实例分别包成两个独立的 {@code FutureTask}，所以任务体会真正执行两次。
 * 若反过来把<b>同一个</b> {@code FutureTask} 提交两次，它只会执行一次，第二次 {@code get()} 拿到的是缓存的同一个结果。
 * <p>
 * 任务返回的是执行它的线程名，因此能直观看到两个任务分别跑在线程池的两个不同工作线程上。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see FutureTaskDemo2
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        demo();
    }

    /**
     * {@code get()} 会阻塞到任务执行完毕，因此方法返回前两行输出已经打印完成。
     * 线程名取决于线程池编号，但两行都以「 执行成功！」结尾
     */
    public static void demo() throws ExecutionException, InterruptedException {
        // 创建FutureTask
        Task task = new Task();
        FutureTask<String> f1 = new FutureTask<>(task);
        FutureTask<String> f2 = new FutureTask<>(task);

        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(f1);
        executor.submit(f2);
        System.out.println(f1.get());
        System.out.println(f2.get());
        executor.shutdown();
    }

    static class Task implements Callable<String> {

        @Override
        public String call() {
            return Thread.currentThread().getName() + " 执行成功！";
        }

    }

}
// 输出（线程池编号取决于当前 JVM 已创建过多少个线程池）：
// pool-1-thread-1 执行成功！
// pool-1-thread-2 执行成功！
