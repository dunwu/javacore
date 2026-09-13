package io.github.dunwu.javacore.concurrent.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 演示线程池使用不当引发的 OutOfMemoryError，以及手动指定各项参数的正确做法。
 * <p>
 * {@code oom1()} 用 {@code newFixedThreadPool}：其工作队列是无界的 {@code LinkedBlockingQueue}，
 * 任务会无限堆积在队列中直至堆耗尽；
 * {@code oom2()} 用 {@code newCachedThreadPool}：其最大线程数为 {@code Integer.MAX_VALUE}，
 * 任务会无限创建线程直至内存耗尽。
 * <p>
 * {@code right()} 手动指定核心/最大线程数、有界队列与拒绝策略（{@code AbortPolicy}）；
 * {@code better()} 在此基础上改写队列的 {@code offer} 语义（总是返回 false 制造队满假象），
 * 让线程池优先扩容到最大线程数、扩不动了再入队。
 * <p>
 * 注：oom1/oom2 会真实耗尽内存，main 中默认已注释掉，仅调用 {@code right()}。
 * <p>
 * 注：{@code main} 返回后进程不会退出（JDK 21 实测：{@code right()} 的 main 约 80 秒返回，之后进程仍存活
 * 并持续每秒打印线程池状态）。两处都指向非守护线程未终止：{@code printStats} 用
 * {@code newSingleThreadScheduledExecutor} 注册了每秒重复的任务且从未 shutdown
 * （{@code Executors.defaultThreadFactory} 创建的是非守护线程）；线程池自身的核心线程也默认不会因
 * keepAliveTime 超时回收。需手动终止进程。
 * <p>
 * 注：{@code right()} 的线程池为 core=2 / max=5 / 队列容量 10 / AbortPolicy，而每个任务耗时 10 秒、
 * 每秒提交一个，因此从第 16 个左右开始会被拒绝。JDK 21 实测一次：17 个任务正常 started/finished，
 * 其余 3 次打印 {@code error submitting task ...RejectedExecutionException}（17 + 3 = 20）。
 * 三次拒绝都报 {@code id=18}，是因为 catch 里的 {@code decrementAndGet()} 把编号回退后又被下一轮复用。
 * <p>
 * 注：hutool 的 {@code setNamePrefix} 并不做 {@code %d} 格式化（那是 Guava 的约定），
 * 因此线程名会原样带上 {@code %d}。
 */
public class ThreadPoolOOM {

    public static void main(String[] args) throws InterruptedException {
        // oom1();
        // oom2();
        right();
    }

    private static void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("=========================");
            System.out.println("Pool Size: " + threadPool.getPoolSize());
            System.out.println("Active Threads: " + threadPool.getActiveCount());
            System.out.println("Number of Tasks Completed: " + threadPool.getCompletedTaskCount());
            System.out.println("Number of Tasks in Queue: " + threadPool.getQueue().size());

            System.out.println("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void oom1() throws InterruptedException {

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        printStats(threadPool);
        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000000)
                    .mapToObj(__ -> "a")
                    .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                }
                System.out.println(payload);
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void oom2() throws InterruptedException {

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        printStats(threadPool);
        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                }
                System.out.println(payload);
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    public static int right() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            2, 5,
            5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNamePrefix("demo-threadpool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());
        //threadPool.allowCoreThreadTimeOut(true);
        printStats(threadPool);
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    System.out.println(id + " started");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(id + " finished");
                });
            } catch (Exception ex) {
                // 保留异常信息：slf4j 原本会打印完整栈轨迹，这里至少输出异常类型与 message
                System.out.println("error submitting task " + id + " " + ex);
                atomicInteger.decrementAndGet();
            }
        });

        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

    public static int better() throws InterruptedException {
        //这里开始是激进线程池的实现
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10) {
            @Override
            public boolean offer(Runnable e) {
                //先返回false，造成队列满的假象，让线程池优先扩容
                return false;
            }
        };

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            2, 5,
            5, TimeUnit.SECONDS,
            queue, new ThreadFactoryBuilder().setNamePrefix("demo-threadpool-%d").build(), (r, executor) -> {
            try {
                //等出现拒绝后再加入队列
                //如果希望队列满了阻塞线程而不是抛出异常，那么可以注释掉下面三行代码，修改为executor.getQueue().put(r);
                if (!executor.getQueue().offer(r, 0, TimeUnit.SECONDS)) {
                    throw new RejectedExecutionException("ThreadPool queue full, failed to offer " + r.toString());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        //激进线程池实现结束

        printStats(threadPool);
        //每秒提交一个任务，每个任务耗时10秒执行完成，一共提交20个任务

        //任务编号计数器
        AtomicInteger atomicInteger = new AtomicInteger();

        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    System.out.println(id + " started");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(id + " finished");
                });
            } catch (Exception ex) {
                // 保留异常信息：slf4j 原本会打印完整栈轨迹，这里至少输出异常类型与 message
                System.out.println("error submitting task " + id + " " + ex);
                atomicInteger.decrementAndGet();
            }
        });

        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

}
