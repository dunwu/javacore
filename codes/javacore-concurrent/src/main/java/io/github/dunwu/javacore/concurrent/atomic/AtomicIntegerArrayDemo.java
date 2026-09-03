package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * {@link java.util.concurrent.atomic.AtomicIntegerArray} 示例
 * <p>
 * 对数组做无锁的原子操作：{@code incrementAndGet(i)} 原子地自增下标 i 处的元素，
 * {@code compareAndSet(i, expect, update)} 仅当下标 i 处的元素等于 expect 时才把它改为 update。
 * <p>
 * 两个线程并发操作同一个数组：{@link Increment} 把每个元素加 1，{@link Compare} 尝试把值为 2 的元素改成 3。
 * 因为两个线程没有先后顺序，{@code compareAndSet} 可能成功（抢在自增前）也可能失败（下标 2 已被改成 3），
 * 所以「swapped」那一行可能输出也可能不输出，最终数组的内容也因此不固定。
 * 这正是 CAS 的典型语义：失败不报错，而是直接返回 false，由调用方决定要不要重试。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 */
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void demo() throws InterruptedException {

        System.out.println("Init Values: ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i, i);
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
        System.out.println();

        Thread t1 = new Thread(new Increment());
        Thread t2 = new Thread(new Compare());
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Values: ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
        System.out.println();
    }

    public static void main(final String[] arguments) throws InterruptedException {
        demo();
    }

    static class Increment implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                int value = atomicIntegerArray.incrementAndGet(i);
                System.out.println(Thread.currentThread().getName() + ", index = " + i + ", value = " + value);
            }
        }

    }

    static class Compare implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                boolean swapped = atomicIntegerArray.compareAndSet(i, 2, 3);
                if (swapped) {
                    System.out.println(Thread.currentThread().getName() + " swapped, index = " + i + ", value = 3");
                }
            }
        }

    }

}
// 输出（中间部分的线程名、行顺序以及「swapped」行是否出现都取决于调度，Final Values 也因此不固定）：
// Init Values:
// 0 1 2 3 4 5 6 7 8 9
// Thread-1 swapped, index = 2, value = 3
// Thread-0, index = 0, value = 1
// ...
// Final Values:
// 1 2 4 4 5 6 7 8 9 10
