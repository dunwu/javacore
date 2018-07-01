package io.github.dunwu.javacore.concurrent;

/**
 * @author Zhang Peng
 */
public class ThreadLocalDemo01 {
    ThreadLocal<Long> longLocal = new ThreadLocal<>();
    ThreadLocal<String> stringLocal = new ThreadLocal<>();

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
        final ThreadLocalDemo01 threadLocalDemo = new ThreadLocalDemo01();

        threadLocalDemo.set();
        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());

        Thread thread1 = new Thread(() -> {
            threadLocalDemo.set();
            System.out.println(threadLocalDemo.getLong());
            System.out.println(threadLocalDemo.getString());
        });
        thread1.start();
        thread1.join();

        System.out.println(threadLocalDemo.getLong());
        System.out.println(threadLocalDemo.getString());
    }
}
