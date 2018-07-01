package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author Zhang Peng
 * @date 2018/5/24
 */
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void main(final String[] arguments) throws InterruptedException {

        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i, i);
        }

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
    }

    static class Increment implements Runnable {

        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                int add = atomicIntegerArray.incrementAndGet(i);
                System.out.println(Thread.currentThread().getName() + ", index " + i + ", value: " + add);

            }
        }
    }

    static class Compare implements Runnable {

        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                boolean swapped = atomicIntegerArray.compareAndSet(i, 2, 3);

                if (swapped) {
                    System.out.println(Thread.currentThread().getName() + ", index " + i + ", value: 3");
                }
            }
        }
    }
}
