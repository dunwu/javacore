package io.github.dunwu.javacore.concurrent.tool;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * Semaphore 可以允许多个线程访问一个临界区
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-02
 */
public class Semaphore限流示例 {

    public static void main(String[] args) {
        // 创建对象池，大小为 10
        ObjectPool<Long, String> pool = new ObjectPool<>(10, 2L);
        // 通过对象池获取 t，之后执行
        pool.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
        pool.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
    }

    static class ObjectPool<T, R> {

        final List<T> pool;
        // 用信号量实现限流器
        final Semaphore semaphore;

        // 构造函数
        ObjectPool(int size, T t) {
            pool = new Vector<T>() {};
            for (int i = 0; i < size; i++) {
                pool.add(t);
            }
            semaphore = new Semaphore(size);
        }

        // 利用对象池的对象，调用 func
        R exec(Function<T, R> func) {
            T t = null;

            try {
                semaphore.acquire();
                t = pool.remove(0);
                return func.apply(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                pool.add(t);
                semaphore.release();
                return null;
            }
        }

    }

}
