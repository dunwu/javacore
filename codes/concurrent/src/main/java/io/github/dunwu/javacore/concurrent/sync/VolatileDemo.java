package io.github.dunwu.javacore.concurrent.sync;

/**
 * volatile 修饰符示例
 * 被 volatile 修饰的变量具有两层含义
 * 保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
 * 禁止进行指令重排序
 *
 * @author Zhang Peng
 * @date 2018/5/10
 */
public class VolatileDemo {

    private volatile int inc = 0;

    private void increase() {
        inc++;
    }

    public static void main(String[] args) {
        final VolatileDemo demo = new VolatileDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) { demo.increase(); }
            }).start();
        }

        while (Thread.activeCount() > 1)  // 保证前面的线程都执行完
        { Thread.yield(); }
        System.out.println(demo.inc);
    }
}
