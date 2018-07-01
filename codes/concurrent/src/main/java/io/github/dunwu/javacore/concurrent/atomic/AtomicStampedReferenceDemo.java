package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference 可以解决 CAS 中的 ABA 问题
 * @author Zhang Peng
 * @date 2018/5/24
 */
public class AtomicStampedReferenceDemo {

    private final static String INIT_REF = "abc";

    public static void main(String[] args) throws InterruptedException {

        AtomicStampedReference<String> asr = new AtomicStampedReference<>(INIT_REF, 0);
        System.out.println("初始对象为：" + asr.getReference());
        final int stamp = asr.getStamp();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(Math.abs((int) (Math.random() * 100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (asr.compareAndSet(INIT_REF, Thread.currentThread().getName(), stamp, stamp + 1)) {
                    System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                    System.out.println("新的对象为：" + asr.getReference());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}
