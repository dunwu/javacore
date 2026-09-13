package io.github.dunwu.javacore.concurrent.lock;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 演示对多把 {@link ReentrantLock} 加锁时顺序不当引发的问题，以及按固定顺序加锁的修复方案。
 * <p>
 * {@code wrong()} 按购物车中商品的随机顺序依次加锁，并发下两个线程可能各自持有对方需要的锁而互相等待；
 * {@code right()} 先按商品名排序再加锁，保证全局加锁顺序一致，从根本上消除互相等待。
 * <p>
 * 注：{@code createOrder} 用的是带 10 秒超时的 {@code tryLock}，因此 {@code wrong()} 不会永久挂起：
 * 超时后会释放已持有的锁并返回下单失败，表现为 success 数下降、耗时上升。
 * 输出中的 took 与 success 依赖线程调度，每次运行都不同，不可作为断言依据。
 * <p>
 * {@code main} 中默认只执行 {@code right()}；如需观察 wrong 的表现，请放开 {@code cart.wrong()} 的注释。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
public class ReentrantLockDeadlockDemo {

    public static void main(String[] args) {
        ShopCart cart = new ShopCart();
        // cart.wrong();
        cart.right();
    }

    static class ShopCart {

        private ConcurrentHashMap<String, Item> items = new ConcurrentHashMap<>();

        public ShopCart() {
            IntStream.range(0, 10).forEach(i -> items.put("item" + i, new Item("item" + i)));
        }

        private boolean createOrder(List<Item> order) {
            List<ReentrantLock> locks = new ArrayList<>();

            for (Item item : order) {
                try {
                    if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
                        locks.add(item.lock);
                    } else {
                        locks.forEach(ReentrantLock::unlock);
                        return false;
                    }
                } catch (InterruptedException e) {
                }
            }
            try {
                order.forEach(item -> item.remaining--);
            } finally {
                locks.forEach(ReentrantLock::unlock);
            }
            return true;
        }

        private List<Item> createCart() {
            return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(items.size()))
                .map(name -> items.get(name)).collect(Collectors.toList());
        }

        public long wrong() {
            long begin = System.currentTimeMillis();
            long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
            // " took:" 后面的减法必须加括号：否则会被解析为 ("..." + System.currentTimeMillis()) - begin，无法编译
            System.out.println("success:" + success + " totalRemaining:"
                + items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum)
                + " took:" + (System.currentTimeMillis() - begin) + "ms items:" + items);
            return success;
        }

        public long right() {
            long begin = System.currentTimeMillis();
            long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream()
                        .sorted(Comparator.comparing(Item::getName))
                        .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
            System.out.println("success:" + success + " totalRemaining:"
                + items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum)
                + " took:" + (System.currentTimeMillis() - begin) + "ms items:" + items);
            return success;
        }

    }

    @Data
    @RequiredArgsConstructor
    static class Item {

        final String name;
        int remaining = 1000;
        @ToString.Exclude
        ReentrantLock lock = new ReentrantLock();

        @Override
        public String toString() {
            return "{" +
                "name='" + name + '\'' +
                ", remaining=" + remaining +
                '}';
        }

    }

}
