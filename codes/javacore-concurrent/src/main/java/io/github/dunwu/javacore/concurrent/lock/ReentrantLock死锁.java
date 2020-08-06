package io.github.dunwu.javacore.concurrent.lock;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
@Slf4j
public class ReentrantLock死锁 {

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
            log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
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
            log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
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
