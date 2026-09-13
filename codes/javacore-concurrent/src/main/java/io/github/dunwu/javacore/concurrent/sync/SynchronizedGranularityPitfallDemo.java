package io.github.dunwu.javacore.concurrent.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 演示 synchronized 锁粒度过大导致的并发度下降。
 * <p>
 * {@code wrong()} 把耗时的 {@code slow()}（每次 sleep 10ms）也包进了 {@code synchronized (this)}，
 * 1000 个并行任务全部串行等待同一把锁，总耗时不低于 1000 × 10ms；
 * {@code right()} 只锁住真正需要互斥的 {@code data.add(i)}，{@code slow()} 在锁外并行执行，耗时大幅下降。
 * <p>
 * JDK 21 实测一次：{@code wrong()} 输出 {@code took:15540}，{@code right()} 输出 {@code took:742}，
 * 相差 20 倍以上（另一次实测为 {@code took:15548} 与 {@code took:680}）；
 * wrong 超出 10000ms 的部分是锁竞争与上下文切换开销。
 * <p>
 * 注：{@code right()} 的实际耗时取决于 ForkJoinPool 公共池的并行度（即 CPU 核数），
 * 每次运行结果都不同，上述数值不可作为断言依据。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
public class SynchronizedGranularityPitfallDemo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.wrong();
        demo.right();
    }

    private static class Demo {

        private List<Integer> data = new ArrayList<>();

        private void slow() {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        public int wrong() {
            long begin = System.currentTimeMillis();
            IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
                synchronized (this) {
                    slow();
                    data.add(i);
                }
            });
            // 减法必须加括号：否则会被解析为 ("took:" + System.currentTimeMillis()) - begin，无法编译
            System.out.println("took:" + (System.currentTimeMillis() - begin));
            return data.size();
        }

        public int right() {
            long begin = System.currentTimeMillis();
            IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
                slow();
                synchronized (data) {
                    data.add(i);
                }
            });
            System.out.println("took:" + (System.currentTimeMillis() - begin));
            return data.size();
        }

    }

}
