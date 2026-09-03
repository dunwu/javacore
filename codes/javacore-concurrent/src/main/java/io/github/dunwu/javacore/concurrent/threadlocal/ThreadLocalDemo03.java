package io.github.dunwu.javacore.concurrent.threadlocal;

import java.util.function.Supplier;

/**
 * {@link ThreadLocal#withInitial(java.util.function.Supplier)} 示例：给副本提供默认值，避开拆箱 NPE
 * <p>
 * {@link ThreadLocalDemo02} 用的是不带初始值的 {@code ThreadLocal}，未 {@code set} 就 {@code get} 会得到 {@code null}。
 * 本例的 {@code getLong()} 返回的是基本类型 {@code long}，如果 {@code get()} 返回 {@code null}，
 * 拆箱时就会直接抛 {@link NullPointerException}。用 {@code withInitial} 提供 Supplier 后，
 * 任何线程首次 {@code get()} 都会先执行一次 Supplier 拿到默认值，因此永远不会是 {@code null}。
 * <p>
 * 本例的 Supplier 返回当前线程的 ID 与线程名，所以输出可以验证两件事：
 * <ol>
 *     <li>不同线程拿到的是<b>不同</b>的默认值（证明副本确实按线程隔离）</li>
 *     <li>子线程 {@code set} 之后，主线程再 {@code get} 仍是自己的值（证明子线程的写入不会影响主线程）</li>
 * </ol>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadLocalDemo02
 */
public class ThreadLocalDemo03 {

    private ThreadLocal<Long> longLocal = ThreadLocal.withInitial(new Supplier<Long>() {
        @Override
        public Long get() {
            return Thread.currentThread().getId();
        }
    });

    private ThreadLocal<String> stringLocal = ThreadLocal.withInitial(new Supplier<String>() {
        @Override
        public String get() {
            return Thread.currentThread().getName();
        }
    });

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 输出 6 行，依次是：主线程的 ID/线程名、子线程的 ID/线程名、主线程再读一次的 ID/线程名。
     * 具体的 ID 和线程名取决于 JVM 已创建过多少线程，但第 1、2 行必然与第 5、6 行完全相等
     */
    public static void demo() throws InterruptedException {
        final ThreadLocalDemo03 threadLocalDemo = new ThreadLocalDemo03();

        threadLocalDemo.set();
        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocalDemo.set();
                System.out.println(threadLocalDemo.getLong());
                System.out.println(threadLocalDemo.getString());
            }
        });
        thread.start();
        thread.join();

        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());

        threadLocalDemo.longLocal.remove();
        threadLocalDemo.stringLocal.remove();
    }

    private void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    private long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

}
