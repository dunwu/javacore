package io.github.dunwu.javacore.concurrent.threadlocal;

import java.util.function.Supplier;

/**
 * 在main线程中，没有先set，直接get的话，运行时会报空指针异常。
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
