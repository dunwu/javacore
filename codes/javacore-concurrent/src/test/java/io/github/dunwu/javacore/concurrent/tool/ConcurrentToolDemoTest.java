package io.github.dunwu.javacore.concurrent.tool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * tool 包示例单元测试（FutureTask 与基于 Semaphore 的限流器）
 * <p>
 * {@code FutureTask} 的 {@code get()} 会阻塞到任务执行完毕，因此这些示例的输出天然是完整且可断言的。
 * 唯一的非确定性来自线程名：线程池编号（{@code pool-N}）与线程序号（{@code Thread-N}）都是 JVM 全局递增的，
 * 取决于此前已经创建过多少线程池/线程，所以只断言格式而不断言具体编号。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("tool 包：FutureTask 与限流器示例测试")
public class ConcurrentToolDemoTest {

    @Test
    @DisplayName("FutureTask 交给线程池：两个独立的 FutureTask 各执行一次，get() 分别拿到两个工作线程的返回值")
    void testFutureTaskDemo() {
        String output = capture(() -> FutureTaskDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(2);
        // 返回值就是执行该任务的工作线程名，因此能看出任务确实跑在了线程池的线程上，而不是调用者线程
        assertThat(out).allMatch(line -> line.matches("pool-\\d+-thread-\\d+ 执行成功！"));
    }

    @Test
    @DisplayName("FutureTask 交给普通线程：FutureTask 实现了 Runnable，可直接作为 Thread 的构造参数")
    void testFutureTaskDemo2() {
        String output = capture(() -> FutureTaskDemo2.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(2);
        assertThat(out).allMatch(line -> line.matches("Thread-\\d+ 执行成功！"));
        // new Thread(f1) 与 new Thread(f2) 是两个不同的线程，所以两行返回值必然不同
        assertThat(out[0]).isNotEqualTo(out[1]);
    }

    @Test
    @DisplayName("FutureTask 汇总多个 Callable 的结果：1..100 与 101..200 的和相加等于 20100")
    void testFutureTaskDemo3() {
        String output = capture(() -> FutureTaskDemo3.demo());
        assertThat(output).isEqualTo("value1 = 5050, value2 = 15050\ntotal = 20100\n");
    }

    @Test
    @DisplayName("Semaphore 限流器：池中有 10 个对象，20 次调用都能借到并归还，因此恰好输出 20 行")
    void testSemaphoreRateLimit() {
        String output = capture(() -> SemaphoreRateLimit.demo());
        String[] out = lines(output);

        // 若 exec 的 finally 中漏掉 release()，第 11 次调用就会永久阻塞在 acquire() 上，测试将超时失败
        assertThat(out).hasSize(20);
        assertThat(out).containsOnly("2");
    }

}
