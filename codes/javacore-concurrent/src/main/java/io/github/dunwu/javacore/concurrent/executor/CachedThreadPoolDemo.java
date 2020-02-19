package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link Executors#newCachedThreadPool} 示例
 * <p>
 * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class CachedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            });
        }
        executorService.shutdown();
    }

}
