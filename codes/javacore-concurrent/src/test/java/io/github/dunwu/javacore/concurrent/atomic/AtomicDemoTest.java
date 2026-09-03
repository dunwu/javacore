package io.github.dunwu.javacore.concurrent.atomic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.capture;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.lines;
import static io.github.dunwu.javacore.concurrent.DemoOutputCapture.nonBlankLines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * atomic 包示例单元测试
 * <p>
 * <b>断言策略</b>：并发示例的输出往往混杂了「确定不变的部分」与「取决于线程调度的部分」，
 * 因此这里只对前者做精确断言，对后者只断言其必须满足的不变量（行数、格式、出现次数），
 * 绝不断言具体的线程名或行的先后顺序，避免产生偶发失败的 flaky 测试。
 * <p>
 * 各示例的 {@code demo()} 都已改造为「返回前等待自己创建的全部线程结束」，
 * 且会重置内部的静态状态，因此可以重复调用、也不依赖测试方法的执行顺序。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("atomic 包：原子类示例测试")
public class AtomicDemoTest {

    /**
     * 线程池工作线程名的通用格式，形如 pool-1-thread-2
     */
    private static final String POOL_THREAD = "pool-\\d+-thread-\\d+";

    @Test
    @DisplayName("AtomicInteger：incrementAndGet 是原子操作，10 个任务并发自增的结果稳定为 10")
    void testAtomicIntegerDemo() {
        String output = capture(() -> AtomicIntegerDemo.demo());
        assertThat(output).isEqualTo("Final Count is : 10\n");
    }

    @Test
    @DisplayName("AtomicIntegerArray：初始化输出确定；自增与 CAS 并发竞争，导致 swapped 行可能出现也可能不出现")
    void testAtomicIntegerArrayDemo() {
        String output = capture(() -> AtomicIntegerArrayDemo.demo());

        // 初始化阶段是单线程的，输出完全确定（注意 "Init Values: " 和数字行都带一个尾随空格）
        assertThat(output).startsWith("Init Values: \n0 1 2 3 4 5 6 7 8 9 \n");

        String[] all = nonBlankLines(output);
        // 2 行初始化 + 10 行自增 + 0~1 行 CAS 成功 + 2 行最终值
        assertThat(all).hasSizeBetween(14, 15);
        assertThat(all[all.length - 2]).isEqualTo("Final Values: ");
        assertThat(all[all.length - 1]).matches("(\\d+ ){10}");

        // Increment 线程必然把 10 个下标各打印一次
        assertThat(all).filteredOn(line -> line.matches("Thread-\\d+, index = \\d+, value = \\d+"))
                       .hasSize(10);
        // Compare 线程的 CAS 最多成功一次：一旦下标 2 的值被自增改成 3，compareAndSet(i, 2, 3) 就不可能再成功
        assertThat(all).filteredOn(line -> line.contains("swapped"))
                       .hasSizeLessThanOrEqualTo(1);
    }

    @Test
    @DisplayName("AtomicMarkableReference：10 个任务争抢同一个 CAS，只有第一个到达的能成功")
    void testAtomicMarkableReferenceDemo() {
        String output = capture(() -> AtomicMarkableReferenceDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(2);
        assertThat(out[0]).matches(POOL_THREAD + " 修改了对象！");
        // CAS 成功后引用被换成了胜出线程的名字，所以第二行打印的就是它
        String winner = out[0].substring(0, out[0].indexOf(' '));
        assertThat(out[1]).isEqualTo("新的对象为：" + winner);
    }

    @Test
    @DisplayName("AtomicStampedReference：用版本号解决 ABA，初始引用确定，且只有一个线程能改成功")
    void testAtomicStampedReferenceDemo() {
        String output = capture(() -> AtomicStampedReferenceDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(3);
        // INIT_REF 是硬编码的常量字符串，因此这一行完全确定
        assertThat(out[0]).isEqualTo("初始对象为：pool-1-thread-3");
        assertThat(out[1]).matches(POOL_THREAD + " 修改了对象！");
        String winner = out[1].substring(0, out[1].indexOf(' '));
        assertThat(out[2]).isEqualTo("新的对象为：" + winner);
    }

    @Test
    @DisplayName("AtomicReferenceFieldUpdater：5 个任务争抢一次 CAS，1 个成功 4 个失败，成功者因 sleep 必然最后打印")
    void testAtomicReferenceFieldUpdaterDemo() {
        String output = capture(() -> AtomicReferenceFieldUpdaterDemo.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(5);
        assertThat(out).filteredOn(line -> line.endsWith("已被其他线程修改")).hasSize(4);
        assertThat(out).filteredOn(line -> line.endsWith("已修改 name = end")).hasSize(1);
        // 抢到锁的线程会先 sleep 1 秒再打印，其余 4 个立即打印，所以成功那一行必然在最后
        assertThat(out[4]).matches(POOL_THREAD + " 已修改 name = end");
    }

    @Test
    @DisplayName("AtomicReference 反例：卖票不做任何同步，第一张卖出的必然是 10 号票，且总行数不少于 10")
    void testAtomicReferenceDemo() {
        String output = capture(() -> AtomicReferenceDemo.demo());
        String[] out = lines(output);

        // 每轮循环先打印再自减，所以打印次数 = 自减次数 = 10 - 最终票号；票号最多被减到 0，故行数 >= 10
        assertThat(out).hasSizeGreaterThanOrEqualTo(10);
        assertThat(out).allMatch(line -> line.matches(POOL_THREAD + " 卖出了第 -?\\d+ 张票"));
        // 第一次打印之前不可能有任何自减发生，所以第一个读到的票号必然是初始值 10
        assertThat(output).contains("卖出了第 10 张票");
    }

    @Test
    @DisplayName("AtomicReference 正例：自旋锁把「判断 + 打印 + 自减」包成临界区，10 张票恰好各卖一次")
    void testAtomicReferenceDemo2() {
        String output = capture(() -> AtomicReferenceDemo2.demo());
        String[] out = lines(output);

        assertThat(out).hasSize(10);
        List<Integer> tickets = Arrays.stream(out)
                                      .map(line -> line.substring(line.indexOf("第 ") + 2, line.indexOf(" 张票")))
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());
        // 与反例的关键差异：没有重复票号，也没有把票减成负数
        assertThat(tickets).containsExactlyInAnyOrder(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    }

    @Test
    @DisplayName("AtomicReference API：线程启动前的初始状态确定；原子引用只替换引用，不会拷贝对象")
    void testAtomicReferenceDemo3() {
        String output = capture(() -> AtomicReferenceDemo3.demo());
        String[] all = nonBlankLines(output);

        // 4 行初始状态 + 两个线程各 2 行 + 4 行最终状态
        assertThat(all).hasSize(12);
        assertThat(all[0]).isEqualTo("Message is: hello");
        assertThat(all[1]).isEqualTo("Person is [name Phillip, age 23]");
        assertThat(all[2]).isEqualTo("Atomic Reference of Message is: hello");
        assertThat(all[3]).isEqualTo("Atomic Reference of Person is [name Phillip, age 23]");

        // 中间 4 行是两个子线程打印的，但两个线程可能互相交织，因此只能断言格式与各类行的数量
        String[] middle = Arrays.copyOfRange(all, 4, 8);
        assertThat(middle).allMatch(line -> line.matches("Thread-\\d+ (Values:?|Atomic References:?) .*"));
        // 两个线程各打印一行 Values、一行 Atomic References
        assertThat(middle).filteredOn(line -> line.contains("Values")).hasSize(2);
        assertThat(middle).filteredOn(line -> line.contains("Atomic References")).hasSize(2);
        // Values 行打印的是普通变量 message，它总是在初始值 "hello" 上拼接而成
        // （Atomic References 行打印的是原子引用，可能已被 lazySet 成 "Thread 2"，所以不含 hello）
        assertThat(middle).filteredOn(line -> line.contains("Values"))
                          .allMatch(line -> line.contains("hello"));

        // 末尾 4 行是 join 之后打印的，此时两个线程都已结束
        assertThat(all[8]).startsWith("Now Message is: hello");
        assertThat(all[9]).startsWith("Person is [name Thread ");
        assertThat(all[10]).startsWith("Atomic Reference of Message is: ");
        assertThat(all[11]).startsWith("Atomic Reference of Person is [name Thread ");
    }

}
