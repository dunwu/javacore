package io.github.dunwu.javacore.concurrent.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link ThreadLocal} 正确示例：每个线程拥有自己的变量副本，互不干扰
 * <p>
 * {@code threadLocal} 重写了 {@code initialValue()} 返回 0，因此任何线程第一次 {@code get()}
 * 都会拿到属于自己的 0，而不是 {@code null}（若不重写 {@code initialValue}，直接拆箱会抛 NPE）。
 * <p>
 * 10 个线程各自从 0 累加 10 次，因为副本彼此隔离，每个线程打印的都是 {@code count = 10}。
 * 对比 {@link ThreadLocalErrorDemo}：那里用的是共享静态变量，打印出的值远大于 10 且各不相同。
 * <p>
 * 末尾的 {@code remove()} 很重要：线程池会复用线程，不清理会导致下一个任务读到上一个任务的残留值
 * （典型的「用户串号」问题），也可能因为持有大对象而造成内存泄漏。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadLocalErrorDemo
 */
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 10 个线程并发累加各自的 ThreadLocal 副本，输出稳定为 10 行 {@code count = 10}。
     * 末尾的 {@code awaitTermination} 保证方法返回前所有输出已经打印完毕
     */
    public static void demo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
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
