package io.github.dunwu.javacore.container.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.EmptyStackException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * javacore-container queue 包示例的单元测试
 */
@DisplayName("队列与栈示例测试")
public class QueueDemoTest {

    @Test
    @DisplayName("LinkedList 实现队列：offer 入队、poll 出队、element/peek 查看队首")
    public void testLinkedListQueueDemo() {
        String output = captureOutput(LinkedListQueueDemo::demo);
        assertThat(output).contains("poll=a");
        assertThat(output).contains("element=b");
        assertThat(output).contains("peek=b");
    }

    @Test
    @DisplayName("栈只有 3 个元素却 pop 4 次，第 4 次抛 EmptyStackException（反例）")
    public void testStackDemo() {
        assertThatThrownBy(() -> StackDemo.main(new String[0]))
            .isInstanceOf(EmptyStackException.class);
    }

    @Test
    @DisplayName("PriorityQueue：默认最小堆，peek 不移除、poll 按优先级依次取出")
    void testPriorityQueueNaturalOrder() {
        String output = captureOutput(PriorityQueueDemo::naturalOrder);
        assertThat(output).isEqualTo("入队顺序: [5, 1, 3]\n"
            + "peek 只看队首、不移除: 1\n"
            + "peek 之后队列大小不变: 3\n"
            + "poll 依次取出: [1, 3, 5]\n"
            + "取空后 peek 返回: null\n"
            + "取空后 poll 返回: null\n");
    }

    @Test
    @DisplayName("PriorityQueue：iterator/toString 是堆的存储顺序，与优先级顺序无关")
    void testPriorityQueueIterationOrder() {
        String output = captureOutput(PriorityQueueDemo::iterationOrderIsNotPriorityOrder);
        // 只断言规范保证的部分：poll 顺序必然按优先级递增
        assertThat(output).contains("poll 出队顺序: [1, 2, 3, 5, 7, 9]");
        // 以及核心结论：遍历顺序与之不同。堆的内部存储顺序属于实现细节，因此不精确断言
        assertThat(output).contains("遍历顺序与出队顺序是否一致: false");
    }

    @Test
    @DisplayName("PriorityQueue：Comparator 可翻转成最大堆，也可按派生属性排序")
    void testPriorityQueueCustomComparator() {
        String output = captureOutput(PriorityQueueDemo::customComparator);
        assertThat(output).isEqualTo("入队顺序: [5, 1, 3]\n"
            + "大顶堆 poll 顺序: [5, 3, 1]\n"
            + "按字符串长度升序 poll: [C, Java, Python]\n");
    }

    @Test
    @DisplayName("PriorityQueue：自定义对象用 Comparable 或 Comparator 决定出队顺序")
    void testPriorityQueueCustomObject() {
        String output = captureOutput(PriorityQueueDemo::customObject);
        assertThat(output).isEqualTo("按 Comparable 出队: [修线上故障, 代码评审, 写文档]\n"
            // 同一个类换一个 Comparator 就得到完全相反的顺序
            + "按 Comparator 倒序出队: [写文档, 代码评审, 修线上故障]\n");
    }

    @Test
    @DisplayName("PriorityQueue：容量为 K 的最小堆求 Top-K，只占 O(K) 内存")
    void testPriorityQueueTopK() {
        String output = captureOutput(PriorityQueueDemo::topK);
        assertThat(output).isEqualTo("数据: [3, 9, 1, 7, 5, 8, 2]\n"
            + "最大的 3 个: [7, 8, 9]\n");
    }

    @Test
    @DisplayName("PriorityQueue：不允许 null；无界意味着 offer 永远成功")
    void testPriorityQueueBoundaries() {
        String output = captureOutput(PriorityQueueDemo::boundaries);
        assertThat(output).isEqualTo("offer(null) 抛出: NullPointerException\n"
            + "初始容量给 1，装入 100 个元素全部成功: true\n"
            + "实际元素个数: 100\n"
            + "PriorityQueue 非线程安全，并发场景应改用 PriorityBlockingQueue\n");
    }

    @Test
    @DisplayName("ArrayDeque：两端都能 O(1) 进出，peek 不移除")
    void testArrayDequeBothEnds() {
        String output = captureOutput(ArrayDequeDemo::bothEnds);
        assertThat(output).isEqualTo("依次 offerFirst(b)、offerFirst(a)、offerLast(c) 后: [a, b, c]\n"
            + "peekFirst（不移除）: a\n"
            + "peekLast（不移除）: c\n"
            + "peek 之后大小不变: 3\n"
            + "pollFirst: a\n"
            + "pollLast: c\n"
            + "两端各取出一个后剩下: [b]\n");
    }

    @Test
    @DisplayName("ArrayDeque 当栈：push/pop 作用于队首，因此后进先出")
    void testArrayDequeAsStack() {
        String output = captureOutput(ArrayDequeDemo::asStack);
        assertThat(output).isEqualTo("依次 push 1、2、3 后: [3, 2, 1]\n"
            + "peek（看栈顶不弹出）: 3\n"
            + "pop: 3\n"
            + "pop: 2\n"
            + "弹出两次后剩下: [1]\n"
            + "表达式 ((a+b)*[c-d]) 的括号是否匹配: true\n"
            + "表达式 ((a+b)*[c-d) 的括号是否匹配: false\n");
    }

    @Test
    @DisplayName("ArrayDeque 当队列：offer/poll 一头进另一头出，可直接用于层序遍历")
    void testArrayDequeAsQueue() {
        String output = captureOutput(ArrayDequeDemo::asQueue);
        assertThat(output).isEqualTo("依次 offer a、b 后: [a, b]\n"
            + "poll: a\n"
            + "取出一个后剩下: [b]\n"
            + "层序遍历结果: [1, 2, 3, 4, 5, 6, 7, 8, 9]\n");
    }

    @Test
    @DisplayName("ArrayDeque：不允许 null，因为 null 是「队列为空」的返回信号")
    void testArrayDequeNullNotAllowed() {
        String output = captureOutput(ArrayDequeDemo::nullNotAllowed);
        assertThat(output).isEqualTo("offer(null) 抛出: NullPointerException\n"
            + "push(null) 抛出: NullPointerException\n"
            + "空队列 pollFirst 返回: null\n"
            + "空队列 peekFirst 返回: null\n"
            // 与遗留的 Stack.pop() 抛 EmptyStackException 不同
            + "空队列 pop 会抛异常: NoSuchElementException\n");
    }

    @Test
    @DisplayName("ArrayDeque：正反两个方向都能遍历，扩容对外透明且不打乱顺序")
    void testArrayDequeIterationAndGrowth() {
        String output = captureOutput(ArrayDequeDemo::iterationAndGrowth);
        assertThat(output).isEqualTo("初始长度给 2，装入 3 个元素后: [a, b, c]\n"
            + "iterator 正向遍历: [a, b, c]\n"
            + "descendingIterator 反向遍历: [c, b, a]\n"
            + "扩容没有打乱顺序: true\n");
    }

    @Test
    @DisplayName("BlockingQueue：add/remove/element 抛异常，offer/poll/peek 返回特殊值")
    void testBlockingQueueFourMethodGroups() {
        String output = captureOutput(BlockingQueueDemo::fourMethodGroups);
        assertThat(output).isEqualTo("add(a): true\n"
            + "add(b): true\n"
            + "队列满时 add 抛出: IllegalStateException\n"
            + "队列满时 offer 返回: false\n"
            + "peek 查看队首（不移除）: a\n"
            + "poll 取出队首: a\n"
            + "poll 之后剩下: [b]\n"
            + "清空后 poll 返回: null\n"
            + "清空后 peek 返回: null\n"
            + "空队列 remove 抛出: NoSuchElementException\n"
            + "空队列 element 抛出: NoSuchElementException\n");
    }

    @Test
    @DisplayName("BlockingQueue：满时 put 阻塞、空时 take 阻塞，这正是生产者-消费者的基础")
    void testBlockingQueueBlockingBehaviour() {
        String output = captureOutput(BlockingQueueDemo::blockingBehaviour);
        assertThat(output).isEqualTo("容量为 1 的队列放入一个元素后，剩余容量: 0\n"
            + "take 取到: first\n"
            + "队列满时 put 会一直阻塞，直到有元素被 take 走\n"
            + "再 take 取到: second\n"
            + "空队列上 take 会一直阻塞，放入元素后取到: data\n");
    }

    @Test
    @DisplayName("BlockingQueue：带超时的 offer/poll 等满指定时长后返回 false 或 null")
    void testBlockingQueueTimeoutMethods() {
        String output = captureOutput(BlockingQueueDemo::timeoutMethods);
        assertThat(output).isEqualTo("队列满时 offer(50ms) 的结果: false\n"
            + "是否等满了超时时间才返回: true\n"
            + "空队列上 poll(50ms) 的结果: null\n"
            + "是否等满了超时时间才返回: true\n");
    }

    @Test
    @DisplayName("BlockingQueue：LinkedBlockingQueue 默认无界，这正是禁用 Executors 的根因")
    void testBlockingQueueBoundedVsUnbounded() {
        String output = captureOutput(BlockingQueueDemo::boundedVsUnbounded);
        assertThat(output).isEqualTo("ArrayBlockingQueue 必须指定容量，此处剩余容量: 5\n"
            // 关键结论：2147483647 等于无界，任务堆积会一路吃到内存耗尽
            + "LinkedBlockingQueue 不指定容量时默认剩余容量: 2147483647\n"
            + "PriorityBlockingQueue 无界，剩余容量: 2147483647\n"
            + "SynchronousQueue 不存储元素，剩余容量: 0\n"
            + "Executors.newFixedThreadPool 默认队列的剩余容量: 2147483647\n");
    }

    @Test
    @DisplayName("BlockingQueue：六种实现的选型，以及 SynchronousQueue 的直接交接")
    void testBlockingQueueImplementationGuide() {
        String output = captureOutput(BlockingQueueDemo::implementationGuide);
        assertThat(output).contains("常见 BlockingQueue 实现的选型对照：")
            .contains("没有消费者在等时，SynchronousQueue.offer 返回: false")
            .contains("SynchronousQueue 的 size 恒为: 0")
            // 关键结论：它没有任何存储空间，只能一手交一手
            .contains("有消费者在等时，offer 完成直接交接: true")
            .contains("消费者收到的内容: data");
    }

    @Test
    @DisplayName("三个新示例的 demo：完整演示均可正常执行且不残留线程")
    void testAllDemos() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(PriorityQueueDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(ArrayDequeDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(BlockingQueueDemo::demo)).doesNotThrowAnyException();
    }

    /**
     * 允许抛出受检异常的无参代码块。BlockingQueueDemo 的示例涉及 put / take，会抛 InterruptedException
     */
    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new IllegalStateException("示例执行失败：" + e.getMessage(), e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

}
