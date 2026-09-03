package io.github.dunwu.javacore.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * {@link java.util.concurrent.atomic.AtomicReferenceFieldUpdater} 示例
 * <p>
 * 它不包装对象，而是直接对某个类的 <b>volatile 实例字段</b> 做原子更新。
 * 相比为每个对象都建一个 {@code AtomicReference}，它的优势是<b>不额外占用内存</b>：
 * 更新器本身是无状态的单例，字段仍是普通的 {@code volatile String}，
 * 在需要海量对象（如每个连接、每个节点）都做原子更新时能显著降低开销。
 * <p>
 * 使用限制：目标字段必须同时满足 {@code volatile}、非 {@code static}、且对更新器所在类可访问，
 * 否则 {@code newUpdater} 会抛 {@link IllegalArgumentException}。
 * <p>
 * 5 个任务争抢把 {@code user.name} 从 {@code begin} CAS 成 {@code end}，只有一个能成功，
 * 其余 4 个打印「已被其他线程修改」。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-26
 */
public class AtomicReferenceFieldUpdaterDemo {

    static User user = new User("begin");

    static AtomicReferenceFieldUpdater<User, String> updater =
        AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 5 个任务在 3 个线程上争抢一次 CAS，只有抢到的那个会打印「已修改」。
     * 抢到的线程会先 sleep 1 秒再打印，因此线程名不固定，但「已修改」有且仅有一行
     */
    public static void demo() throws InterruptedException {
        // 重置为初始状态，保证本方法可以重复调用（否则第二次调用时 name 已是 end，所有 CAS 都会失败）
        user = new User("begin");

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            if (updater.compareAndSet(user, "begin", "end")) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 已修改 name = " + user.getName());
            } else {
                System.out.println(Thread.currentThread().getName() + " 已被其他线程修改");
            }
        }

    }

    static class User {

        volatile String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

    }

}
