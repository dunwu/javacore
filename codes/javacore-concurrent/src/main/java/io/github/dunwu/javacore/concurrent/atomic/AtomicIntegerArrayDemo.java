package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * {@link java.util.concurrent.atomic.AtomicIntegerArray} 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 */
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void main(final String[] arguments) throws InterruptedException {

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
