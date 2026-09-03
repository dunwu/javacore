package io.github.dunwu.javacore.concurrent.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
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
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
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
                    log.info("batch file processing done");
                });
            }
        }).start();
    }

}
