package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

@ThreadSafe
public class ThreadSafeCounter {

    private static long count = 0;

    private synchronized void add() {
        int cnt = 0;
        while (cnt++ < 100000) {
            count += 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
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

}
// 输出：
// count = 200000
