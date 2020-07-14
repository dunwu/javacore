package io.github.dunwu.javacore.concurrent.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            int count = threadLocal.get();
            for (int i = 0; i < 10; i++) {
                try {
                    count++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadLocal.set(count);
            threadLocal.remove();
            System.out.println("count = " + count);
        }

    }

}
// 全部输出 count = 10
