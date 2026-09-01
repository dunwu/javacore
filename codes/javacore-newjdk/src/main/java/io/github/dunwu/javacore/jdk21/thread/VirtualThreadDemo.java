package io.github.dunwu.javacore.jdk21.thread;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Java 21 虚拟线程（Virtual Threads）示例。
 * <p>
 * 虚拟线程在 Java 19/20 预览、Java 21 正式转正（JEP 444，Project Loom 的核心成果）。
 * <ul>
 * <li>虚拟线程是由 JVM 调度的轻量级线程，创建成本极低（KB 级栈内存），可轻松创建百万级</li>
 * <li>平台线程（传统线程）1:1 映射到操作系统内核线程，数量受限（通常数千个）</li>
 * <li>适合"大量任务 + 大量阻塞 IO"的高并发服务端场景：阻塞时虚拟线程自动让出载体线程（carrier thread）</li>
 * </ul>
 * 注意：虚拟线程不是让单个任务更快，而是提升整体吞吐量；CPU 密集型任务并无收益。
 */
public class VirtualThreadDemo {

    /**
     * 示例 1：Thread.ofVirtual() 构建器方式创建虚拟线程
     */
    public static void ofVirtualBuilder() throws InterruptedException {
        Thread vt = Thread.ofVirtual()
            .name("my-virtual-thread")
            .start(() -> {
                Thread current = Thread.currentThread();
                System.out.println(current.getName() + " 是虚拟线程吗: " + current.isVirtual());
            });
        vt.join();
    }

    /**
     * 示例 2：Thread.startVirtualThread() 快捷方式
     */
    public static void startVirtualThreadShortcut() throws InterruptedException {
        Thread vt2 = Thread.startVirtualThread(
            () -> System.out.println("startVirtualThread 创建: " + Thread.currentThread().isVirtual()));
        vt2.join();
    }

    /**
     * 示例 3：每个任务一个虚拟线程的执行器（推荐用于服务端请求处理模型）。
     * 1000 个任务每个睡眠 100ms：若用少量平台线程串行执行需 100 秒，虚拟线程可几乎并发完成
     */
    public static void virtualThreadPerTaskExecutor() throws InterruptedException {
        int taskCount = 1000;
        CountDownLatch latch = new CountDownLatch(taskCount);
        long start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> executor.submit(() -> {
                try {
                    Thread.sleep(Duration.ofMillis(100));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }));
            latch.await();
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(taskCount + " 个各睡眠 100ms 的任务总耗时: " + elapsed + " ms");
        System.out.println("远小于串行所需的 " + taskCount * 100 + " ms，说明任务近乎完全并发");
    }

    /**
     * 示例 4：平台线程对比——同样任务用平台线程验证虚拟线程的标识差异
     */
    public static void platformThreadComparison() {
        Thread platform = Thread.ofPlatform().unstarted(() -> {
        });
        System.out.println("平台线程 isVirtual: " + platform.isVirtual());
    }

    public static void main(String[] args) throws InterruptedException {
        ofVirtualBuilder();
        startVirtualThreadShortcut();
        virtualThreadPerTaskExecutor();
        platformThreadComparison();
    }

}
