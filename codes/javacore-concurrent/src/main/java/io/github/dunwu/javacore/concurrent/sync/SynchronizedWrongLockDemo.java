package io.github.dunwu.javacore.concurrent.sync;

import lombok.Getter;

import java.util.stream.IntStream;

/**
 * 反例：synchronized 保护的对象不对。
 * <p>
 * {@code wrong()} 把 synchronized 加在实例方法上，而 parallel stream 每次迭代的都是
 * <b>新建的 Data 实例</b>，于是每个线程锁的是不同对象，根本互斥不了，counter 会丢失更新；
 * {@code right()} 改为锁静态的 locker，所有线程竞争同一把锁，结果才是正确的。
 * <p>
 * 运行结果：wrong 的输出远小于 1000000（JDK 21 实测一次为 54383，丢失了约 94.5% 的更新；
 * 具体值随线程调度而变，不可作为断言依据），right 的输出恒为 1000000。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
public class SynchronizedWrongLockDemo {

    public static void main(String[] args) {
        SynchronizedWrongLockDemo demo = new SynchronizedWrongLockDemo();
        System.out.println(demo.wrong(1000000));
        System.out.println(demo.right(1000000));
    }

    public int wrong(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().wrong());
        return Data.getCounter();
    }

    public int right(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().right());
        return Data.getCounter();
    }

    private static class Data {

        @Getter
        private static int counter = 0;
        private static Object locker = new Object();

        public static int reset() {
            counter = 0;
            return counter;
        }

        public synchronized void wrong() {
            counter++;
        }

        public void right() {
            synchronized (locker) {
                counter++;
            }
        }

    }

}
