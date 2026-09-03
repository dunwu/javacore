package io.github.dunwu.javacore.concurrent.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * error 包示例单元测试：并发编程典型错误的正反例对比
 * <p>
 * <b>关于「反例」的断言尺度</b>：{@link NotThreadSafeCounter} 与 {@link WrongResult} 演示的是丢失更新，
 * 丢失多少次完全取决于线程调度，因此<b>不能</b>断言某个具体数值，也<b>不宜</b>断言「一定小于预期值」——
 * 理论上如果两个线程恰好完全错开执行，结果也可能正好等于预期值，那样就会得到偶发失败的 flaky 测试。
 * 所以这里只断言必然成立的边界（结果落在 (0, 预期值] 区间内），并在注释里记录实测观察到的量级。
 * <p>
 * 与之相对，{@link ThreadSafeCounter} 的结果是<b>完全确定</b>的 200000，可以做精确断言 ——
 * 这个「反例只能弱断言、正例可以强断言」的对比本身，就是同步措施是否生效最直观的体现。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("error 包：并发错误示例正反对比测试")
public class ConcurrencyErrorDemoTest {

    /**
     * 两个线程各自累加的次数
     */
    private static final int TIMES_PER_THREAD = 100000;

    private static final int EXPECTED_TOTAL = TIMES_PER_THREAD * 2;

    @Test
    @DisplayName("正例：synchronized 保证 count += 1 的原子性，结果稳定为 200000")
    void testThreadSafeCounter() {
        String output = capture(() -> ThreadSafeCounter.demo());
        assertThat(output).isEqualTo("count = " + EXPECTED_TOTAL + "\n");
    }

    @Test
    @DisplayName("反例：无同步的 count += 1 会丢失更新，结果落在 (0, 200000] 区间且通常远小于 200000")
    void testNotThreadSafeCounter() {
        String output = capture(() -> NotThreadSafeCounter.demo());
        long count = parseCount(output);

        // 必然成立的边界：两个线程合计最多执行 20 万次自增，且至少成功了一部分
        assertThat(count).isBetween(1L, (long) EXPECTED_TOTAL);
        // 实测这个值通常在 10 万到 19 万之间（例如某次运行为 113266），远小于 200000，
        // 但具体数值随机器与调度而变，故不做精确断言
    }

    @Test
    @DisplayName("反例：volatile 只保证可见性，不保证 i++ 的原子性，结果落在 (0, 20000] 区间")
    void testWrongResult() {
        String output = capture(() -> WrongResult.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(1);
        assertThat(out[0]).endsWith("，预期值 = 20000");
        int actual = Integer.parseInt(out[0].substring("i = ".length(), out[0].indexOf('，')));
        assertThat(actual).isBetween(1, 20000);
    }

    @Test
    @DisplayName("反例：在构造器中启动线程做初始化，不 join 就读取会拿到 null；join 之后结果稳定")
    void testWrongInit() {
        String output = capture(() -> WrongInit.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(3);
        // 第一行的结果取决于线程调度：子线程还没赋值时抛 NPE，碰巧赋值完成时则能读到值。
        // 这种「有时报错有时正常」正是并发缺陷最难排查的特征，所以两种结果都算符合预期
        assertThat(out[0]).startsWith("未等待初始化线程就读取 students.get(1) ");
        assertThat(out[0]).containsAnyOf("抛出 NullPointerException", "= 王小美");

        // join 建立了 happens-before：子线程的写入对主线程可见，因此后两行完全确定
        assertThat(out[1]).isEqualTo("等待初始化线程结束后读取 students.get(1) = 王小美");
        assertThat(out[2]).isEqualTo("students = {1=王小美, 2=钱二宝, 3=周三, 4=赵四}");
    }

    private static long parseCount(String output) {
        String[] out = lines(output);
        assertThat(out).hasSize(1);
        assertThat(out[0]).startsWith("count = ");
        return Long.parseLong(out[0].substring("count = ".length()));
    }

}
