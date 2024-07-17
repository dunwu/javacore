package io.github.dunwu.javacore.concurrent.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileDemo {

    public volatile static int count = 0;

    public void add() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        VolatileDemo volatileAtomicityDemo = new VolatileDemo();
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                for (int j = 0; j < 500; j++) {
                    volatileAtomicityDemo.add();
                }
            });
        }
        // 等待 1.5 秒，保证上面程序执行完成
        Thread.sleep(1500);
        System.out.println(count);
        threadPool.shutdown();
    }

}