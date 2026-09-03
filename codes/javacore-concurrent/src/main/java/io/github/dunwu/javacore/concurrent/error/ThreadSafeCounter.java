package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

/**
 * 正确示例：用 {@code synchronized} 保证复合操作的原子性
 * <p>
 * 与 {@link NotThreadSafeCounter} 唯一的差别就是 {@code add()} 上加了 {@code synchronized}：
 * 同一时刻只有一个线程能进入该方法，因此「读取 - 加一 - 写回」三步不会被其他线程打断，
 * 不会发生丢失更新。两个线程各执行 10 万次，结果稳定为 200000。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see NotThreadSafeCounter
 */
@ThreadSafe
public class ThreadSafeCounter {

    private static long count = 0;

    private synchronized void add() {
        int cnt = 0;
        while (cnt++ < 100000) {
            count += 1;
        }
    }

    public static void demo() throws InterruptedException {
        // 重置计数器，保证本方法可以重复调用（否则多次调用的结果会累加）
        count = 0;

        final ThreadSafeCounter demo = new ThreadSafeCounter();
        // 创建两个线程，执行 add() 操作
        Thread t1 = new Thread(() -> {
            demo.add();
        });
        Thread t2 = new Thread(() -> {
            demo.add();
        });
        // 启动两个线程
        t1.start();
        t2.start();
        // 等待两个线程执行结束
        t1.join();
        t2.join();
        System.out.println("count = " + count);
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}
// Output:
// count = 200000
