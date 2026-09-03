package io.github.dunwu.javacore.concurrent.tool.sync;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * tool.sync 包示例单元测试（CountDownLatch / CyclicBarrier / Exchanger / Semaphore）
 * <p>
 * 这几个并发工具的价值恰恰体现在「输出的<b>位置</b>是确定的，而<b>内容</b>（线程名、行顺序）不确定」：
 * <ul>
 *     <li>{@code CountDownLatch.await()} 之后的输出必然排在所有 {@code countDown()} 之前的输出后面</li>
 *     <li>{@code CyclicBarrier} 的 barrierAction 必然排在所有参与线程到达之后</li>
 *     <li>{@code Exchanger} 两侧各自的内部顺序是固定的，只有两侧之间的交织位置不确定</li>
 * </ul>
 * 因此下面的断言一律针对「由并发工具的语义所保证的位置关系」，而不去断言具体线程名，
 * 这样既验证了工具确实生效，又不会因为调度差异而偶发失败。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("tool.sync 包：并发协作工具示例测试")
public class SyncToolDemoTest {

    @Test
    @DisplayName("CountDownLatch：主线程 await 到计数归零才继续，因此首行与末两行的位置是固定的")
    void testCountDownLatchDemo() {
        String output = capture(() -> CountDownLatchDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(7);
        assertThat(out[0]).isEqualTo("等待2个子线程执行完毕...");
        // 这两行在 latch.await() 返回之后才打印，而 await 返回意味着两个子线程都已 countDown，
        // 也即两个子线程的「执行完毕」都已打印 —— 这正是 CountDownLatch 的语义保证
        assertThat(out[5]).isEqualTo("2个子线程已经执行完毕");
        assertThat(out[6]).isEqualTo("继续执行主线程");
        // 中间 4 行的先后顺序与线程名取决于调度
        assertThat(out[1]).matches("子线程Thread-\\d+正在执行");
        assertThat(out[2]).matches("子线程Thread-\\d+正在执行");
        assertThat(out[3]).matches("子线程Thread-\\d+执行完毕");
        assertThat(out[4]).matches("子线程Thread-\\d+执行完毕");
    }

    @Test
    @DisplayName("CountDownLatch 起跑门 + 终点门：3 个线程同时起跑，各打印 5 行，末行是并发耗时")
    void testCountDownLatchDemo02() {
        String output = capture(() -> CountDownLatchDemo02.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(16);
        for (int i = 0; i < 15; i++) {
            assertThat(out[i]).matches("Thread-\\d+运行，i = [1-5]");
        }
        // 末行是 endGate 归零后计算的毫秒耗时，数值取决于机器性能
        assertThat(out[15]).matches("\\d+");
        // 起跑门保证 3 个线程都真正参与并发执行：15 行里恰好出现 3 个不同的线程名，每个各 5 行
        List<String> threadNames = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            threadNames.add(out[i].substring(0, out[i].indexOf("运行")));
        }
        assertThat(threadNames).hasSize(15);
        assertThat(threadNames.stream().distinct().count()).isEqualTo(3);
        for (String name : threadNames.stream().distinct().toList()) {
            assertThat(threadNames.stream().filter(name::equals).count()).isEqualTo(5);
        }
    }

    @Test
    @DisplayName("CyclicBarrier：4 个线程全部到达后栅栏动作才执行，因此它必然排在最后一行")
    void testCyclicBarrierDemo() {
        String output = capture(() -> CyclicBarrierDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(9);
        // 4 个线程启动后立刻打印「正在写入数据」，然后统一 sleep 3 秒，
        // 所以前 4 行必然是「正在写入数据」，后 4 行必然是「写入数据完毕」
        for (int i = 0; i < 4; i++) {
            assertThat(out[i]).matches("线程Thread-\\d+正在写入数据\\.\\.\\.");
        }
        for (int i = 4; i < 8; i++) {
            assertThat(out[i]).matches("线程Thread-\\d+写入数据完毕，等待其他线程写入完毕");
        }
        // barrierAction 由最后到达的那个线程执行，且在其他线程被释放之前，所以必然是最后一行
        assertThat(out[8]).matches("当前线程Thread-\\d+");
    }

    @Test
    @DisplayName("CyclicBarrier 两道栅栏：两个 barrierAction 的输出位置被严格固定在第 3 行和第 6 行")
    void testCyclicBarrierDemo02() {
        String output = capture(() -> CyclicBarrierDemo02.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(8);
        assertThat(out[0]).matches("Thread-\\d+ waiting at barrier 1");
        assertThat(out[1]).matches("Thread-\\d+ waiting at barrier 1");
        assertThat(out[2]).isEqualTo("BarrierAction 1 executed");
        assertThat(out[3]).matches("Thread-\\d+ waiting at barrier 2");
        assertThat(out[4]).matches("Thread-\\d+ waiting at barrier 2");
        assertThat(out[5]).isEqualTo("BarrierAction 2 executed");
        assertThat(out[6]).matches("Thread-\\d+ done!");
        assertThat(out[7]).matches("Thread-\\d+ done!");
    }

    @Test
    @DisplayName("Exchanger：两侧各自的输出顺序固定，消费者第 N 轮拿到的一定是生产者第 N 轮装入的数据")
    void testExchangerDemo() {
        String output = capture(() -> ExchangerDemo.demo());
        String[] all = lines(output);

        // 生产者每轮 5 行 × 4 轮 = 20 行，消费者每轮 4 行 × 4 轮 = 16 行
        assertThat(all).hasSize(36);
        // 消费者一开始就阻塞在 exchange 上，所以第一行必然是生产者的
        assertThat(all[0]).isEqualTo("生产者第1次提供");

        // 两侧的交织位置取决于调度，但各自内部的顺序是固定的：过滤出来后可以做精确断言
        List<String> produced = filterByPrefix(all, "生产者装入");
        List<String> consumed = filterByPrefix(all, "消费者 : ");
        assertThat(produced).hasSize(12);
        assertThat(consumed).hasSize(12);

        List<String> expected = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                expected.add(i + "--" + j);
            }
        }
        for (int k = 0; k < 12; k++) {
            assertThat(produced.get(k)).isEqualTo("生产者装入" + expected.get(k));
            assertThat(consumed.get(k)).isEqualTo("消费者 : buffer：" + expected.get(k));
        }

        // 生产者与消费者各提取/提供 4 轮
        assertThat(filterByPrefix(all, "生产者第")).hasSize(4);
        assertThat(filterByPrefix(all, "生产者装满")).hasSize(4);
        assertThat(filterByPrefix(all, "消费者第")).hasSize(4);
    }

    @Test
    @DisplayName("Semaphore：30 个线程争抢 10 个许可，最终每个都执行一次，输出恰好 30 行")
    void testSemaphoreDemo() {
        String output = capture(() -> SemaphoreDemo.demo());
        // 打印内容不含线程名，所以 30 行完全一致，整体输出是确定的
        assertThat(output).isEqualTo("save data\n".repeat(30));
    }

    private static List<String> filterByPrefix(String[] all, String prefix) {
        return Arrays.stream(all).filter(line -> line.startsWith(prefix)).toList();
    }

}
