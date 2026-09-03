package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 使用 {@link java.util.concurrent.atomic.AtomicStampedReference} 解决 CAS 中的 ABA 问题
 * <p>
 * ABA 问题：线程 1 读到值 A 后被挂起，线程 2 把值从 A 改成 B 又改回 A，
 * 线程 1 恢复后 CAS 仍然成功——它无法察觉中间发生过变更。
 * <p>
 * 解决办法：给引用额外配一个递增的版本号（stamp），
 * {@code compareAndSet(expectRef, newRef, expectStamp, newStamp)} 要求引用和版本号同时匹配才能更新。
 * 即使值被改回了 A，版本号也已经从 0 变成 2，CAS 会失败。
 * <p>
 * 3 个任务各自随机 sleep 后尝试把 {@code INIT_REF} 改成自己的线程名（同时把 stamp 加一），
 * 只有第一个到达的能成功，因此「修改了对象」有且仅有一行。
 * 对比 {@link AtomicMarkableReferenceDemo}：那里的标记是 boolean，只能区分「动过没动过」，无法区分动过几次。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/24
 * @see AtomicMarkableReferenceDemo
 */
public class AtomicStampedReferenceDemo {

    private final static String INIT_REF = "pool-1-thread-3";

    private final static AtomicStampedReference<String> asr = new AtomicStampedReference<>(INIT_REF, 0);

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 演示 3 个线程带版本号争抢修改同一个引用，只有一个能成功。
     * 因为每个线程 sleep 的时长是随机的，哪个线程抢成功并不固定
     */
    public static void demo() throws InterruptedException {
        // 重置为初始状态，保证本方法可以重复调用（否则第二次调用时引用已不是 INIT_REF，所有 CAS 都会失败）
        asr.set(INIT_REF, 0);

        System.out.println("初始对象为：" + asr.getReference());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(new MyThread());
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(Math.abs((int) (Math.random() * 100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int stamp = asr.getStamp();
            if (asr.compareAndSet(INIT_REF, Thread.currentThread().getName(), stamp, stamp + 1)) {
                System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                System.out.println("新的对象为：" + asr.getReference());
            }
        }

    }

}
