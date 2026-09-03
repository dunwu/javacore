package io.github.dunwu.javacore.concurrent.threadlocal;

/**
 * {@link ThreadLocal} 示例：子线程的 set 对主线程不可见，未 set 就 get 会得到 null
 * <p>
 * 子线程往 {@code longLocal} / {@code stringLocal} 里存入自己的线程 ID 与线程名并打印；
 * 主线程 {@code join()} 等子线程结束后再 {@code get()}，得到的仍然是两个 {@code null}。
 * 这说明 ThreadLocal 的副本是严格按线程隔离的，子线程写的值不会传递给主线程。
 * <p>
 * 隐患：这里用的是不带初始值的 {@code new ThreadLocal<>()}，{@code get()} 会返回 {@code null}。
 * 如果接收方是基本类型（如 {@code long id = longLocal.get()}），拆箱时就会抛 {@link NullPointerException}。
 * 解决办法参见 {@link ThreadLocalDemo03}：用 {@code ThreadLocal.withInitial(...)} 提供默认值。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadLocalDemo03
 */
public class ThreadLocalDemo02 {

    private static ThreadLocal<Long> longLocal = new ThreadLocal<>();

    private static ThreadLocal<String> stringLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 前两行是子线程自己的副本（线程 ID 和线程名，具体数值取决于 JVM 已创建过多少线程），
     * 后两行是主线程的 get 结果，恒为两个 null
     */
    public static void demo() throws InterruptedException {

        Thread thread = new Thread(new MyThread());
        thread.start();
        thread.join();

        System.out.println(longLocal.get());
        System.out.println(stringLocal.get());

        longLocal.remove();
        stringLocal.remove();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            longLocal.set(Thread.currentThread().getId());
            stringLocal.set(Thread.currentThread().getName());
            System.out.println(longLocal.get());
            System.out.println(stringLocal.get());
        }

    }

}
