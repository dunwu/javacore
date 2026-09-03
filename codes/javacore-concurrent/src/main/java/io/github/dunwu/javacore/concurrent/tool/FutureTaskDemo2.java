package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask 交给普通线程执行
 * <p>
 * 因为 {@link FutureTask} 实现了 {@link Runnable}，所以可以直接作为 {@code Thread} 的构造参数，
 * 不必依赖线程池。与 {@link FutureTaskDemo}（交给线程池）对比可以看出：
 * 两种方式下 {@code get()} 的用法完全一致，差别只在于线程的创建与复用方式。
 * <p>
 * 实际开发中应优先用线程池：直接 {@code new Thread} 无法复用线程，也无法控制并发数量。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see FutureTaskDemo
 */
public class FutureTaskDemo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        demo();
    }

    /**
     * {@code get()} 会阻塞到任务执行完毕，因此方法返回前两行输出已经打印完成。
     * 线程名形如 {@code Thread-N}，N 取决于当前 JVM 已创建过多少个线程
     */
    public static void demo() throws InterruptedException, ExecutionException {

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
// 输出（N 为 JVM 全局的线程序号，不一定从 0 开始）：
// Thread-0 执行成功！
// Thread-1 执行成功！
