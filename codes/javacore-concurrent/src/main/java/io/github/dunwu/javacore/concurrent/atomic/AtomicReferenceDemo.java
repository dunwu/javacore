package io.github.dunwu.javacore.concurrent.atomic;

import io.github.dunwu.javacore.concurrent.annotation.Error;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 非线程安全的示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@Error
public class AtomicReferenceDemo {

    private static int ticket = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    /**
     * 不做任何线程安全控制
     */
    static class MyThread implements Runnable {

        @Override
        public void run() {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
