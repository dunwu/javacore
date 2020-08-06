package io.github.dunwu.javacore.concurrent.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
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
                        Files.write(Paths.get("demo.txt"),
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
