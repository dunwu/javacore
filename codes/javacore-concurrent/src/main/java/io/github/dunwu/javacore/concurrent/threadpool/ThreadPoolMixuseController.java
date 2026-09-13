package io.github.dunwu.javacore.concurrent.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * 演示线程池混用导致的问题：同一个线程池既承载业务任务、又承载临时计算任务时，两者会互相争抢。
 * <p>
 * {@code wrong()} 把计算任务提交给 {@code threadPool}（核心与最大线程数都只有 2、队列容量 100、
 * 拒绝策略 CallerRunsPolicy）；{@code right()} 改用独立的 {@code asyncCalcThreadPool}（200 线程），
 * 使计算任务不受业务任务影响。
 * <p>
 * 注：{@code main} 只调用 {@code wrong()}，该方法仅返回计算结果、不打印任何内容，
 * 因此直接运行本类看不到任何输出（JDK 21 实测：stdout 与 stderr 均为 0 字节）。
 * <p>
 * 注：{@code main} 返回后进程也不会退出（实测线程转储中仍有 {@code DestroyJavaVM} 与
 * {@code batchfileprocess-threadpool-%d0}）。原因是 {@code wrong()} 提交任务时让 {@code threadPool} 创建了核心线程，
 * 而 {@code ThreadPoolExecutor} 的核心线程默认不会因 keepAliveTime 超时回收（除非调用
 * {@code allowCoreThreadTimeOut(true)}），该线程又是非守护线程，于是 JVM 一直等待它结束。
 * 需要进程正常退出时应显式调用 {@code shutdown()}。
 * <p>
 * 注：{@code printStats} 与 {@code init} 都不会被 {@code main} 触发；{@code init()} 里那段
 * 「无限循环提交写文件任务」的逻辑一旦执行就永不结束，切勿注册为 Spring bean。
 * <p>
 * 注：hutool 的 {@code setNamePrefix} 并不做 {@code %d} 格式化（那是 Guava {@code ThreadFactoryBuilder}
 * 的约定），因此线程名会原样带上 {@code %d}，实测为 {@code batchfileprocess-threadpool-%d0}。
 */
public class ThreadPoolMixuseController {

    /**
     * 演示用的输出文件。统一写到 {@code target/} 目录下，避免污染仓库工作目录
     * （{@code target} 已被 {@code .gitignore} 忽略，且 {@code mvn clean} 会一并清理）。
     * <p>
     * 注：本类并没有 {@code @Component} 之类的注解，{@link #init()} 不会被 Spring 容器调用，
     * 因此那段「无限循环提交写文件任务」的逻辑默认不会执行；统一路径只是防止有人手动注册该 bean
     * 时把 {@code demo.txt} 写到工作目录。若 {@code target} 目录不存在，{@code Files.write} 会抛
     * IOException 并被下方的 catch 打印，不会导致线程崩溃。
     */
    private static final Path DEMO_FILE = Paths.get("target", "demo.txt");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        wrong();
    }

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
        2, 2,
        1, TimeUnit.HOURS,
        new ArrayBlockingQueue<>(100),
        new ThreadFactoryBuilder().setNamePrefix("batchfileprocess-threadpool-%d").build(),
        new ThreadPoolExecutor.CallerRunsPolicy());

    private static ThreadPoolExecutor asyncCalcThreadPool = new ThreadPoolExecutor(
        200, 200,
        1, TimeUnit.HOURS,
        new ArrayBlockingQueue<>(1000),
        new ThreadFactoryBuilder().setNamePrefix("asynccalc-threadpool-%d").build());

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

    private static Callable<Integer> calcTask() {
        return () -> {
            TimeUnit.MILLISECONDS.sleep(10);
            return 1;
        };
    }

    public static int wrong() throws ExecutionException, InterruptedException {
        return threadPool.submit(calcTask()).get();
    }

    public static int right() throws ExecutionException, InterruptedException {
        return asyncCalcThreadPool.submit(calcTask()).get();
    }

    @PostConstruct
    public void init() {
        printStats(threadPool);

        new Thread(() -> {
            String payload = IntStream.rangeClosed(1, 1_000_000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining(""));
            while (true) {
                threadPool.execute(() -> {
                    try {
                        Files.write(DEMO_FILE,
                            Collections.singletonList(LocalTime.now().toString() + ":" + payload), UTF_8, CREATE,
                            TRUNCATE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("batch file processing done");
                });
            }
        }).start();
    }

}
