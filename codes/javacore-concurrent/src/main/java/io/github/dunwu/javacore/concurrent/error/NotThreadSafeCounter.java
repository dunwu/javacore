package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.NotThreadSafe;

@NotThreadSafe
public class NotThreadSafeCounter {

    private static long count = 0;

    private void add() {
        int cnt = 0;
        while (cnt++ < 100000) {
            count += 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final NotThreadSafeCounter demo = new NotThreadSafeCounter();
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
// count = 156602
// 实际结果远小于预期值 200000
