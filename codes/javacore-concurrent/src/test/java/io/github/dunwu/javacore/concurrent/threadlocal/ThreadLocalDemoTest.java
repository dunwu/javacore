package io.github.dunwu.javacore.concurrent.threadlocal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * threadlocal 包示例单元测试
 * <p>
 * 这一组测试的核心是验证 {@code ThreadLocal} 的<b>线程隔离</b>语义：每个线程读到的是自己的副本，
 * 一个线程的写入不会影响其他线程。为了不把测试写成 flaky，断言分两类：
 * <ul>
 *     <li>与线程编号无关的性质（如「主线程前后两次读到的值相等」「未 set 就 get 得到 null」）做精确断言</li>
 *     <li>具体的线程 ID 与线程名取决于 JVM 已经创建过多少线程，只断言格式，不断言数值</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("threadlocal 包：ThreadLocal 示例测试")
public class ThreadLocalDemoTest {

    @Test
    @DisplayName("正例：10 个线程各自累加自己的副本，输出稳定为 10 行 count = 10")
    void testThreadLocalDemo() {
        String output = capture(() -> ThreadLocalDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(10);
        // initialValue() 返回 0，每个线程从自己的 0 开始加 10 次，副本之间互不干扰
        assertThat(out).containsOnly("count = 10");
    }

    @Test
    @DisplayName("反例：改用共享静态变量后 10 个线程互相干扰，打印值不再是 10")
    void testThreadLocalErrorDemo() {
        String output = capture(() -> ThreadLocalErrorDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(10);
        List<Integer> counts = Arrays.stream(out)
                                     .map(line -> line.substring("count = ".length()))
                                     .map(Integer::parseInt)
                                     .collect(Collectors.toList());
        // 10 个线程合计最多执行 100 次自增，因此任何时刻读到的值都不会超过 100
        assertThat(counts).allSatisfy(count -> assertThat(count).isBetween(1, 100));
        // 与正例的关键差异：共享变量被所有线程累加，最后结束的线程看到的值远大于 10
        int max = counts.stream().mapToInt(Integer::intValue).max().orElse(0);
        assertThat(max).isGreaterThan(10);
    }

    @Test
    @DisplayName("未重写 initialValue：子线程的 set 对主线程不可见，主线程 get 恒为 null")
    void testThreadLocalDemo02() {
        String output = capture(() -> ThreadLocalDemo02.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(4);
        // 前两行是子线程自己的副本：线程 ID（数字）与线程名
        assertThat(out[0]).matches("\\d+");
        assertThat(out[1]).startsWith("Thread-");
        // 后两行是主线程 join 之后再 get 的结果：子线程写入的值不会传递给主线程，
        // 且因为用的是不带初始值的 ThreadLocal，得到的就是 null（若接收方是基本类型，拆箱会抛 NPE）
        assertThat(out[2]).isEqualTo("null");
        assertThat(out[3]).isEqualTo("null");
    }

    @Test
    @DisplayName("withInitial：提供默认值后 get 永不为 null，且子线程的 set 不会改变主线程的值")
    void testThreadLocalDemo03() {
        String output = capture(() -> ThreadLocalDemo03.demo());
        String[] out = lines(output);

        // 依次为：主线程的 ID/线程名、子线程的 ID/线程名、主线程再读一次的 ID/线程名
        assertThat(out).hasSize(6);
        assertThat(out[0]).matches("\\d+");
        assertThat(out[2]).matches("\\d+");
        assertThat(out[4]).matches("\\d+");
        assertThat(out[3]).startsWith("Thread-");

        // 线程 ID 全局唯一，所以子线程的默认值必然不同于主线程的默认值 —— 副本确实按线程隔离
        assertThat(out[2]).isNotEqualTo(out[0]);
        // 最关键的一条：子线程执行完 set 之后，主线程再读到的仍是自己原来的值，
        // 证明子线程的写入完全没有影响主线程
        assertThat(out[4]).isEqualTo(out[0]);
        assertThat(out[5]).isEqualTo(out[1]);
    }

}
