package io.github.dunwu.javacore.concurrent.container;

import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 对比 {@link CopyOnWriteArrayList} 与 {@link Collections#synchronizedList} 的并发读写耗时。
 * <p>
 * {@code testRead()} 先向两个列表各填入 100 万个元素，再并发随机读取 100 万次；
 * {@code testWrite()} 并发写入 10 万次。两者均用 {@code StopWatch} 计时，结果打印到控制台，
 * 整个 main 在 JDK 21 上实测约 2.6 秒跑完。
 * <p>
 * JDK 21 实测一次（取自 {@code StopWatch.prettyPrint()}，单位 ns）：
 *
 * <pre>
 * Read  : copyOnWriteArrayList = 043692400   synchronizedList = 065358300
 * Write : copyOnWriteArrayList = 2315747900  synchronizedList = 008730700
 * </pre>
 *
 * 读时 CopyOnWriteArrayList 略快（读无需加锁），写时则慢 250 倍以上（上面这一次为 265 倍，另一次实测为 254 倍）：
 * 它每次 add 都要复制整个底层数组，单次写入代价随已写入数量线性增长，总代价为 O(n^2)。
 * <p>
 * 注：耗时与机器、线程调度强相关，每次运行结果都不同，上述数值不可作为断言依据。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
public class WrongCopyOnWriteList {

    public static void main(String[] args) {
        testRead();
        testWrite();
    }

    public static Map testWrite() {
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        StopWatch stopWatch = new StopWatch();
        int loopCount = 100000;
        stopWatch.start("Write:copyOnWriteArrayList");
        IntStream.rangeClosed(1, loopCount)
            .parallel()
            .forEach(__ -> copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        stopWatch.start("Write:synchronizedList");
        IntStream.rangeClosed(1, loopCount)
            .parallel()
            .forEach(__ -> synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        Map result = new HashMap();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

    private static void addAll(List<Integer> list) {
        list.addAll(IntStream.rangeClosed(1, 1000000).boxed().collect(Collectors.toList()));
    }

    public static Map testRead() {
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);
        StopWatch stopWatch = new StopWatch();
        int loopCount = 1000000;
        int count = copyOnWriteArrayList.size();
        stopWatch.start("Read:copyOnWriteArrayList");
        IntStream.rangeClosed(1, loopCount)
            .parallel()
            .forEach(__ -> copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();
        stopWatch.start("Read:synchronizedList");
        IntStream.range(0, loopCount)
            .parallel()
            .forEach(__ -> synchronizedList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        Map result = new HashMap();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

}
