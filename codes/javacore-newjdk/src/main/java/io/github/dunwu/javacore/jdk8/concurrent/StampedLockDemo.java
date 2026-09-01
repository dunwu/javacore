package io.github.dunwu.javacore.jdk8.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

/**
 * Java 8 StampedLock 邮戳锁示例。
 * <p>
 * {@link StampedLock} 是 Java 8 新增的读写锁增强版，比 {@code ReentrantReadWriteLock}
 * 多了一种<b>乐观读</b>模式：
 * <ul>
 * <li>{@code readLock() / writeLock()}：悲观读写锁（类似 ReentrantReadWriteLock）</li>
 * <li>{@code tryOptimisticRead()}：乐观读，不加锁，仅获取一个邮戳（stamp）；
 * 读取数据后用 {@code validate(stamp)} 校验期间是否发生过写操作，
 * 校验失败再升级为悲观读锁重读</li>
 * </ul>
 * 适合读多写少的场景（如缓存），乐观读完全无阻塞，读吞吐量显著提升。
 * 注意：StampedLock 不可重入、不支持条件变量（Condition）。
 */
public class StampedLockDemo {

    /**
     * 平面上的一个点，使用 StampedLock 保护坐标读写
     */
    static class Point {

        private final StampedLock lock = new StampedLock();

        private double x;

        private double y;

        void move(double deltaX, double deltaY) {
            // 写锁：独占
            long stamp = lock.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        /**
         * 计算到原点的距离：先乐观读，失败再升级悲观读
         */
        double distanceFromOrigin() {
            // 1. 乐观读：不加锁，仅记录邮戳
            long stamp = lock.tryOptimisticRead();
            double currentX = x;
            double currentY = y;
            // 2. 校验读取期间是否有写操作发生
            if (!lock.validate(stamp)) {
                // 3. 校验失败：升级为悲观读锁，重新读取
                stamp = lock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

    }

    /**
     * 多线程并发读写验证：偶数线程写（每次 +1, +1），奇数线程乐观读，
     * 4 个写线程各写 10 万次后最终坐标应为 (400000, 400000)
     */
    public static void concurrentReadWrite() throws InterruptedException {
        Point point = new Point();
        int threads = 8;
        int times = 10_0000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            boolean writer = i % 2 == 0;
            pool.execute(() -> {
                for (int j = 0; j < times; j++) {
                    if (writer) {
                        point.move(1, 1);
                    } else {
                        double distance = point.distanceFromOrigin();
                        if (distance < 0) {
                            throw new IllegalStateException("距离不可能为负，说明读到脏数据");
                        }
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();

        double actual = point.distanceFromOrigin();
        double expected = Math.sqrt(2.0) * 4 * times;
        System.out.println("并发读写完成，最终到原点距离: " + Math.round(actual));
        System.out.println("与期望值一致: " + (Math.abs(actual - expected) < 1));
    }

    public static void main(String[] args) throws InterruptedException {
        concurrentReadWrite();
    }

}
// Output:
// 并发读写完成，最终到原点距离: 565685
// 与期望值一致: true
