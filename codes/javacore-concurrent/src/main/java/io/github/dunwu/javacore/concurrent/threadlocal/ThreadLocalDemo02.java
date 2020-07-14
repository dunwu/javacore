package io.github.dunwu.javacore.concurrent.threadlocal;

/**
 * {@link ThreadLocal} 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ThreadLocalDemo02 {

    private static ThreadLocal<Long> longLocal = new ThreadLocal<>();

    private static ThreadLocal<String> stringLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

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
