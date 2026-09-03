package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * {@link AtomicMarkableReference} 示例
 * <p>
 * {@code AtomicMarkableReference} 维护「引用 + 一个 boolean 标记」的原子对，
 * {@code compareAndSet(expectRef, newRef, expectMark, newMark)} 只有引用和标记同时符合预期才会更新。
 * <p>
 * 它适用于只关心「引用有没有被动过」的场景（标记只能翻转一次，不记录被动过多少次）；
 * 若需要记录变更次数、彻底解决 ABA 问题，应用 {@link AtomicStampedReferenceDemo} 里的
 * {@link java.util.concurrent.atomic.AtomicStampedReference}（标记是一个递增的 int 版本号）。
 * <p>
 * 10 个任务在 3 个线程上并发尝试把初始值 {@code abc} 改成自己的线程名，由于只有第一个 CAS 能成功
 * （之后引用已不再是 {@code abc}），正常情况下只会有一个线程打印「修改了对象」。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 * @see AtomicStampedReferenceDemo
 */
public class AtomicMarkableReferenceDemo {

    private final static String INIT_TEXT = "abc";

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 演示多个线程用 CAS 争抢修改同一个引用，最终只有一个能成功
     */
    public static void demo() throws InterruptedException {

        final AtomicMarkableReference<String> amr = new AtomicMarkableReference<>(INIT_TEXT, false);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String name = Thread.currentThread().getName();
                    if (amr.compareAndSet(INIT_TEXT, name, amr.isMarked(), !amr.isMarked())) {
                        System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                        System.out.println("新的对象为：" + amr.getReference());
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

}
