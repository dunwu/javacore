package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.NotThreadSafe;

/**
 * 错误示例：不加任何同步措施就并发修改共享变量
 * <p>
 * 两个线程各执行 10 万次 {@code count += 1}，预期结果是 200000。但 {@code count += 1} 实际是
 * 「读取 count - 加一 - 写回 count」三步复合操作，两个线程可能同时读到相同的旧值，各自加一后写回，
 * 导致两次自增只生效一次（即「丢失更新」），所以最终结果必然小于 200000。
 * <p>
 * 丢失的次数取决于线程调度，因此每次运行结果都不一样。正确写法参见 {@link ThreadSafeCounter}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadSafeCounter
 */
@NotThreadSafe
public class NotThreadSafeCounter {

    private static long count = 0;

    private void add() {
        int cnt = 0;
        while (cnt++ < 100000) {
            count += 1;
        }
    }

    public static void demo() throws InterruptedException {
        // 重置计数器，保证本方法可以重复调用（否则多次调用的结果会累加）
        count = 0;

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

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}
// 输出（每次运行都不同，且必然小于预期值 200000）：
// count = 156602
