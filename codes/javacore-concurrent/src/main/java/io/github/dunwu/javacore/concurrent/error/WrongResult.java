package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.Error;

/**
 * 错误示例：误以为 {@code volatile} 能保证 {@code i++} 的原子性
 * <p>
 * {@code volatile} 只保证两件事：可见性（写入立即对其他线程可见）与有序性（禁止指令重排序），
 * 它<b>不保证原子性</b>。{@code i++} 实际是「读取 i - 加一 - 写回 i」三步，两个线程可能同时读到相同的值，
 * 各自加一后写回，于是两次自增只生效一次。
 * <p>
 * 两个线程各执行 10000 次，预期 20000，实际每次运行都小于 20000 且结果不固定。
 * 要让自增真正原子，应使用 {@link java.util.concurrent.atomic.AtomicInteger} 或 {@code synchronized}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see io.github.dunwu.javacore.concurrent.atomic.AtomicIntegerDemo
 */
@Error
public class WrongResult {

    volatile static int i;

    public static void demo() throws InterruptedException {
        // 重置计数器，保证本方法可以重复调用（否则多次调用的结果会累加）
        i = 0;

        Runnable r = () -> {
            for (int j = 0; j < 10000; j++) {
                i++;
            }
        };

        Thread thread1 = new Thread(r);
        thread1.start();
        Thread thread2 = new Thread(r);
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("i = " + i + "，预期值 = 20000");
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}
// 输出（每次运行都不同，且必然小于 20000）：
// i = 15937，预期值 = 20000
