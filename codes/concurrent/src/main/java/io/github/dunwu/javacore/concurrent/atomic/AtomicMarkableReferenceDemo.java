package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author Zhang Peng
 * @date 2018/5/24
 */
public class AtomicMarkableReferenceDemo {

    private final static String INIT_REF = "abc";

    public static void main(String[] args) throws InterruptedException {

        AtomicMarkableReference<String> amr = new AtomicMarkableReference<>("abc", false);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(Math.abs((int) (Math.random() * 100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (amr.compareAndSet(INIT_REF, Thread.currentThread().getName(), amr.isMarked(), !amr.isMarked())) {
                    System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                    System.out.println("新的对象为：" + amr.getReference());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}
