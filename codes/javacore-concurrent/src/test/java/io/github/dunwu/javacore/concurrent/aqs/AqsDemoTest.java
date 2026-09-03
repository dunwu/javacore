package io.github.dunwu.javacore.concurrent.aqs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link AqsDemo} 单元测试：基于 AQS 的自定义同步器（独占模式 {@link Mutex} / 共享模式 {@link BooleanLatch}）。
 * <p>
 * 这些示例都涉及多线程，但断言之所以能做到<b>精确匹配</b>，靠的是两点：
 * <ul>
 *     <li>示例刻意不打印线程名，被同时放行的多个线程输出完全一致，因此顺序无关紧要</li>
 *     <li>每一行的位置都由同步语义强制保证——例如「主线程拿到锁」必然排在「waiter 进入 await」之后，
 *     因为只有 await 才会释放锁</li>
 * </ul>
 * 唯一依赖机器性能的是 {@code tryLock(50ms)} 的超时耗时，示例把它折算成了布尔结论，因此同样是确定的。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("AQS 自定义同步器示例测试")
public class AqsDemoTest {

    @Test
    @DisplayName("独占模式：3 个线程各累加 200 次非原子的 int，靠互斥保证不丢更新")
    void testExclusiveMode() throws Exception {
        String output = capture(AqsDemo::exclusiveMode);
        assertThat(output).isEqualTo("新建的 Mutex 是否已被持有: false\n"
            // 关键结论：counter[0]++ 是读改写三步，没有互斥保护时结果必然小于 600
            + "3 个线程各累加 200 次，最终计数: 600\n");
    }

    @Test
    @DisplayName("同步队列：主线程持锁期间 2 个线程排队，全部跑完后队列归零")
    void testQueuedThreads() throws Exception {
        String output = capture(AqsDemo::queuedThreads);
        assertThat(output).isEqualTo("主线程持锁期间，同步队列里的线程数: 2\n"
            + "同步队列里是否有等待线程: true\n"
            + "两个线程都执行完之后，同步队列里的线程数: 0\n"
            + "此时锁是否仍被持有: false\n");
    }

    @Test
    @DisplayName("获取方式：Mutex 不可重入，与 ReentrantLock 的 holdCount 形成对照")
    void testTryLockAndReentrancy() throws Exception {
        String output = capture(AqsDemo::tryLockAndReentrancy);
        assertThat(output).isEqualTo("空闲时 tryLock(): true\n"
            // 关键结论：Mutex 不判断持有者是否为当前线程，因此同一线程第二次获取直接失败
            + "已持有时再次 tryLock(): false\n"
            + "此时锁仍被持有: true\n"
            + "ReentrantLock 已持有时再次 tryLock(): true\n"
            + "ReentrantLock 的重入层数: 2\n"
            + "释放两次后的重入层数: 0\n"
            + "锁被占用时 tryLock(50ms) 的结果: false\n"
            + "是否等满了超时时间才返回: true\n"
            + "holder 释放之后再 tryLock(): true\n");
    }

    @Test
    @DisplayName("条件变量：signal 只是把线程移回同步队列，它要等 signal 方释放锁才能真正继续")
    void testConditionUsage() throws Exception {
        String[] out = lines(capture(AqsDemo::conditionUsage));

        assertThat(out).hasSize(5);
        assertThat(out[0]).isEqualTo("waiter 拿到锁，即将进入 await");
        // 主线程能拿到锁，恰恰是因为 waiter 已经 await 并释放了锁，所以这两行的先后是语义保证的
        assertThat(out[1]).isEqualTo("main 拿到了锁，这说明 waiter 已经 await 并释放了锁");
        assertThat(out[2]).isEqualTo("main 发出 signal，waiter 被移回同步队列，但要等 main 释放锁才能真正继续");
        assertThat(out[3]).isEqualTo("main 释放锁");
        // waiter 必须在 main 释放锁之后才能重新竞争到锁，因此它必然是最后一行
        assertThat(out[4]).isEqualTo("waiter 被唤醒，重新竞争到锁后继续执行");
    }

    @Test
    @DisplayName("共享模式：一次 signal 放行全部 3 个等待线程，且此后不再阻塞")
    void testSharedMode() throws Exception {
        String[] out = lines(capture(AqsDemo::sharedMode));

        assertThat(out).hasSize(8);
        assertThat(out[0]).isEqualTo("初始时闩是否已打开: false");
        assertThat(out[1]).isEqualTo("signal 之前闩是否已打开: false");
        // 3 个等待线程都在 signal 之后、finished.await() 返回之前被放行，所以必然占据这 3 行；
        // 它们彼此之间的先后取决于调度，但内容完全一致
        assertThat(out[2]).isEqualTo("等待线程被放行");
        assertThat(out[3]).isEqualTo("等待线程被放行");
        assertThat(out[4]).isEqualTo("等待线程被放行");
        assertThat(out[5]).isEqualTo("signal 之后闩是否已打开: true");
        assertThat(out[6]).isEqualTo("闩打开后再调用 await 会立即返回，不会阻塞");
        assertThat(out[7]).isEqualTo("重复 signal 之后闩仍是打开的: true");
    }

    @Test
    @DisplayName("state 语义：同一个 int 在各家同步器里表达的含义完全不同")
    void testStateSemantics() {
        String output = capture(AqsDemo::stateSemantics);
        assertThat(output).isEqualTo("AQS 只维护一个 int state，它的含义完全由子类定义：\n"
            + "  ReentrantLock             —— 当前线程的重入次数，0 表示无人持有\n"
            + "  Semaphore                 —— 剩余可用许可数\n"
            + "  CountDownLatch            —— 剩余计数，减到 0 就永久放行\n"
            + "  ReentrantReadWriteLock    —— 高 16 位是读锁持有计数，低 16 位是写锁重入计数\n"
            + "  ThreadPoolExecutor.Worker —— 同样不可重入：-1 表示线程尚未启动（用于抑制中断），\n"
            + "                              0 表示空闲，1 表示正在执行任务\n"
            + "  本例的 Mutex               —— 只有 0 与 1 两种取值，且不允许重入\n"
            + "  本例的 BooleanLatch        —— 0 表示闩关闭，非 0 表示已打开\n"
            + "写锁重入 2 次、读锁持有 1 次，两个计数互不干扰：\n"
            // 两个计数被打包在同一个 state 的高低 16 位里，因此互不干扰
            + "  getWriteHoldCount(): 2\n"
            + "  getReadHoldCount(): 1\n");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行，且不残留任何线程")
    void testDemo() {
        // 用 capture 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> capture(AqsDemo::demo)).doesNotThrowAnyException();
    }

}
