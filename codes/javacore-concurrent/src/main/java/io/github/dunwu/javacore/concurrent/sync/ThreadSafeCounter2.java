package io.github.dunwu.javacore.concurrent.sync;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * 线程安全的计数器示例 - 使用 synchronized 修饰普通方法
 * <p>
 * 示例说明：
 * <p>
 * 定义一个计数器，循环执行自增操作 100000 次。
 * <p>
 * 启动两个线程并行执行，期望最终值为 200000，实际值也为 200000。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/8/1
 */
@ThreadSafe
public class ThreadSafeCounter2 {

    private static int count = 0;

    public synchronized long get() {
        return count;
    }

    public synchronized static void add() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        final int MAX = 100000;
        ThreadSafeCounter2 instance = new ThreadSafeCounter2();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < MAX; i++) {
                instance.add();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < MAX; i++) {
                instance.add();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("count = " + instance.get());
    }

}
