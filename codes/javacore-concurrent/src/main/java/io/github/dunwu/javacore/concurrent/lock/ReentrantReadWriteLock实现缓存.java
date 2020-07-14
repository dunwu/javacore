package io.github.dunwu.javacore.concurrent.lock;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于 ReadWriteLock 实现一个通用的缓存工具类
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-01-01
 */
public class ReentrantReadWriteLock实现缓存 {

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

        public V get(K key) {
            // 获取读锁
            cacheLock.readLock().lock();
            V value;
            try {
                value = cacheMap.get(key);
                String log = String.format("%s 读数据 %s:%s", Thread.currentThread().getName(), key, value);
                System.out.println(log);
            } finally {
                // 释放读锁
                cacheLock.readLock().unlock();
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
