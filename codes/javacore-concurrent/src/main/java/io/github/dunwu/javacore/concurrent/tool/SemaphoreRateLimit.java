package io.github.dunwu.javacore.concurrent.tool;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * Semaphore 可以允许多个线程访问一个临界区，基于这个特点可以轻松实现一个简单的限流器
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-02
 */
public class SemaphoreRateLimit {

    public static void demo() {
        // 创建对象池，大小为 10
        ObjectPool<Long, String> pool = new ObjectPool<>(10, 2L);
        for (int i = 0; i < 20; i++) {
            // 通过对象池获取 t，之后执行
            pool.exec(t -> {
                System.out.println(t);
                return t.toString();
            });
        }
    }

    public static void main(String[] args) {
        demo();
    }

    static class ObjectPool<T, R> {

        final List<T> pool;
        // 用信号量实现限流器
        final Semaphore sem;

        // 构造函数
        ObjectPool(int size, T t) {
            pool = new Vector<T>() { };
            for (int i = 0; i < size; i++) {
                pool.add(t);
            }
            sem = new Semaphore(size);
        }

        // 利用对象池的对象，调用 func
        // 注意：不能在 finally 中写 return。finally 里的 return 会吞掉 try 块的返回值和未捕获的异常，
        // 导致 exec 永远返回 null，这是一个隐蔽且危害很大的错误写法
        R exec(Function<T, R> func) {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return null;
            }
            T t = null;
            try {
                t = pool.remove(0);
                return func.apply(t);
            } finally {
                // 归还对象并释放许可，无论 func 是否抛异常都必须执行
                pool.add(t);
                sem.release();
            }
        }

    }

}
