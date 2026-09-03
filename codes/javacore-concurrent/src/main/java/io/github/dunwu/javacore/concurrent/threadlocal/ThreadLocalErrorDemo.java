package io.github.dunwu.javacore.concurrent.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link ThreadLocal} 错误示例：本该做线程隔离，却用了一个共享的静态变量
 * <p>
 * 10 个线程各自循环 10 次对 {@code count} 做 {@code ++}，如果每个线程拥有自己的副本，
 * 每个线程最终都应该打印 {@code count = 10}。但 {@code count} 是共享的静态字段，10 个线程的累加互相干扰，
 * 因此打印出的值远大于 10，且每个线程打印的值都不一样（取决于它读取时其他线程已经加了多少）。
 * <p>
 * 另一个隐患：{@code count++} 对 {@link Integer} 而言是「读引用 - 拆箱 - 加一 - 装箱 - 写引用」的复合操作，
 * 既不是原子的，字段也没有 {@code volatile} 保证可见性，所以还存在丢失更新。
 * <p>
 * 正确写法参见 {@link ThreadLocalDemo}：用 {@code ThreadLocal} 为每个线程保存独立副本，输出稳定为 {@code count = 10}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-25
 * @see ThreadLocalDemo
 */
public class ThreadLocalErrorDemo {

    private static Integer count = 0;

    public static void demo() throws InterruptedException {
        // 重置计数器，保证本方法可以重复调用（否则多次调用的结果会累加）
        count = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    count++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("count = " + count);
        }

    }

}
