package io.github.dunwu.javacore.concurrent.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 死锁示例
 *
 * @see synchronized死锁示例修正
 */
@SuppressWarnings("all")
public class synchronized死锁示例 {

    public static void main(String[] args) throws InterruptedException {
        final List<Integer> list1 = new ArrayList<>(Arrays.asList(2, 4, 6, 8, 10));
        final List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 7, 9, 11));

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                moveListItem(list1, list2, 2);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                moveListItem(list2, list1, 9);
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(list1);
        System.out.println(list2);
    }

    private static void moveListItem(List<Integer> from, List<Integer> to, Integer item) {
        log("attempting lock for list", from);
        synchronized (from) {
            log("lock acquired for list", from);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("attempting lock for list ", to);
            synchronized (to) {
                log("lock acquired for list", to);
                if (from.remove(item)) {
                    to.add(item);
                }
                log("moved item to list ", to);
            }
        }
    }

    private static void log(String msg, Object target) {
        System.out.println(Thread.currentThread().getName() + ": " + msg + " " + System.identityHashCode(target));
    }

}
