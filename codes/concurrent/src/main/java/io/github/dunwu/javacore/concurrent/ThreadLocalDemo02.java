package io.github.dunwu.javacore.concurrent;

/**
 * 在main线程中，没有先set，直接get的话，运行时会报空指针异常。
 * @see ThreadLocalDemo03
 * @author Zhang Peng
 */
public class ThreadLocalDemo02 {
    private ThreadLocal<Long> longLocal = new ThreadLocal<>();
    private ThreadLocal<String> stringLocal = new ThreadLocal<>();

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
        final ThreadLocalDemo02 threadLocalDemo = new ThreadLocalDemo02();

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
