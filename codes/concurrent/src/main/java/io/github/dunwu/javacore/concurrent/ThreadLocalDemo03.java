package io.github.dunwu.javacore.concurrent;

/**
 * 在main线程中，没有先set，直接get的话，运行时会报空指针异常。
 * @see ThreadLocalDemo02
 * @author Zhang Peng
 */
public class ThreadLocalDemo03 {
    private ThreadLocal<Long> longLocal = ThreadLocal.withInitial(() -> Thread.currentThread().getId());
    private ThreadLocal<String> stringLocal = ThreadLocal.withInitial(() -> Thread.currentThread().getName());

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

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalDemo03 threadLocalDemo = new ThreadLocalDemo03();

        threadLocalDemo.set();
        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());

        Thread thread = new Thread(() -> {
            threadLocalDemo.set();
            System.out.println(threadLocalDemo.getLong());
            System.out.println(threadLocalDemo.getString());
        });
        thread.start();
        thread.join();

        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());

        threadLocalDemo.longLocal.remove();
        threadLocalDemo.stringLocal.remove();
    }
}
