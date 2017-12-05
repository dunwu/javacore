package io.github.dunwu.javase.concurrent.thread;

/**
 * @author Zhang Peng
 */
public class ThreadLocalDemo {
    ThreadLocal<Long> longLocal = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return Thread.currentThread().getId();
        }
    };
    ThreadLocal<String> stringLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalDemo test = new ThreadLocalDemo();

        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getString());

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                test.set();
                System.out.println(test.getLong());
                System.out.println(test.getString());
            };
        };

        thread1.start();
        thread1.join();

        System.out.println(test.getLong());
        System.out.println(test.getString());
    }
}
