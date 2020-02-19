package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用 ThreadPoolExecutor 创建线程池示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 500, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(new MyThread());
            String info = String.format("线程池中线程数目：%s，队列中等待执行的任务数目：%s，已执行玩别的任务数目：%s",
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getQueue().size(),
                threadPoolExecutor.getCompletedTaskCount());
            System.out.println(info);
        }
        threadPoolExecutor.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 执行");
        }

    }

}


