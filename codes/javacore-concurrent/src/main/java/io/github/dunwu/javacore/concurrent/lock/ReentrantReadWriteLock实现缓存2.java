package io.github.dunwu.javacore.concurrent.lock;

import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于 ReadWriteLock 实现一个通用的缓存工具类
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-01-01
 */
public class ReentrantReadWriteLock实现缓存2 {

    static UnboundedCache<Integer, Integer> cache = new UnboundedCache<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new MyThread());
            cache.get(0);
        }
        executorService.shutdown();
    }

    /**
     * 简单的无界缓存实现
     * <p>
     * 使用 WeakHashMap 存储键值对。WeakHashMap 中存储的对象是弱引用，JVM GC 时会自动清除没有被引用的弱引用对象。
     */
    static class UnboundedCache<K, V> {

        private final WeakHashMap<K, V> cacheMap = new WeakHashMap<>();

        private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();

        // 原因是在高并发的场景下，有可能会有多线程竞争写锁。
        //
        // 假设缓存是空的，没有缓存任何东西，如果此时有三个线程 T1、T2 和 T3 同时调用 get() 方法，并且参数 key 也是相同的。
        //
        // 那么它们会同时执行到代码⑤处，但此时只有一个线程能够获得写锁，假设是线程 T1，线程 T1 获取写锁之后查询数据库并更新缓存，最终释放写锁。
        //
        // 此时线程 T2 和 T3 会再有一个线程能够获取写锁，假设是 T2，如果不采用再次验证的方式，此时 T2 会再次查询数据库。
        //
        // T2 释放写锁之后，T3 也会再次查询一次数据库。而实际上线程 T1 已经把缓存的值设置好了，T2、T3 完全没有必要再次查询数据库。
        // 所以，再次验证的方式，能够避免高并发场景下重复查询数据的问题。
        public V get(K key) {
            V value = null;

            // 获取读锁
            cacheLock.readLock().lock();
            try {
                value = cacheMap.get(key);
                String log = String.format("%s 读数据 %s:%s", Thread.currentThread().getName(), key, value);
                System.out.println(log);
            } finally {
                // 释放读锁
                cacheLock.readLock().unlock();
            }

            // 缓存中存在，返回
            if (value != null) {
                return value;
            }

            // 缓存中不存在，查询数据库
            cacheLock.writeLock().lock();
            try {
                // 再次验证
                // 其他线程可能已经查询过数据库
                value = cacheMap.get(key);
                if (value == null) {
                    // 模拟查询数据库
                    cacheMap.put(key, value);
                }
            } finally {
                cacheLock.writeLock().unlock();
            }
            return value;
        }

        public V put(K key, V value) {
            // 获取写锁
            cacheLock.writeLock().lock();
            try {
                cacheMap.put(key, value);
                String log = String.format("%s 写入数据 %s:%s", Thread.currentThread().getName(), key, value);
                System.out.println(log);
            } finally {
                // 释放写锁
                cacheLock.writeLock().unlock();
            }
            return value;
        }

        public V remove(K key) {
            // 获取写锁
            cacheLock.writeLock().lock();
            try {
                return cacheMap.remove(key);
            } finally {
                // 释放写锁
                cacheLock.writeLock().unlock();
            }
        }

        public void clear() {
            // 获取写锁
            cacheLock.writeLock().lock();
            try {
                this.cacheMap.clear();
            } finally {
                // 释放写锁
                cacheLock.writeLock().unlock();
            }
        }

    }

    /**
     * 线程任务每次向缓存中写入 3 个随机值，key 固定
     */
    static class MyThread implements Runnable {

        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                cache.put(i, random.nextInt(100));
            }
        }

    }

}
