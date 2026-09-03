package io.github.dunwu.javacore.container.queue;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 示例：BlockingQueue 阻塞队列。
 * <p>
 * 阻塞队列在普通队列之上多了一条核心语义：<b>队列满时放入会阻塞，队列空时取出会阻塞</b>。
 * 正是这一条让「生产者-消费者」模型不再需要手写 {@code wait} / {@code notify}——
 * 线程间的等待与唤醒全部由队列内部完成，代码因此既简短又不会写错。
 * <p>
 * BlockingQueue 提供了<b>四组</b>语义不同的方法，选错一组就会出现「莫名其妙抛异常」或
 * 「莫名其妙一直卡住」：
 * <table border="1">
 *     <caption>四组方法对照</caption>
 *     <tr><th>操作</th><th>抛异常</th><th>返回特殊值</th><th>一直阻塞</th><th>超时退出</th></tr>
 *     <tr><td>入队</td><td>{@code add}</td><td>{@code offer}</td><td>{@code put}</td>
 *         <td>{@code offer(e, time, unit)}</td></tr>
 *     <tr><td>出队</td><td>{@code remove}</td><td>{@code poll}</td><td>{@code take}</td>
 *         <td>{@code poll(time, unit)}</td></tr>
 *     <tr><td>查看</td><td>{@code element}</td><td>{@code peek}</td><td>—</td><td>—</td></tr>
 * </table>
 * <p>
 * 分工说明：本类讲的是<b>队列本身的 API 与选型</b>；用它搭建生产者-消费者流水线的完整例子见
 * javacore-concurrent 的 {@code container/ArrayBlockingQueueDemo} 与 {@code example/ProducerConsumerDemo0X}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class BlockingQueueDemo {

    /**
     * 超时方法的等待时长
     */
    private static final long WAIT_MILLIS = 50;

    /**
     * SynchronousQueue 直接交接的等待上限，实际几毫秒内就会完成
     */
    private static final long HANDOFF_TIMEOUT_SECONDS = 2;

    /**
     * ① 四组方法中「抛异常」与「返回特殊值」两组的对照（单线程即可完整演示）
     */
    public static void fourMethodGroups() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

        // 第一组：失败时抛异常
        System.out.println("add(a): " + queue.add("a"));
        System.out.println("add(b): " + queue.add("b"));
        try {
            queue.add("c");   // 队列已满
        } catch (IllegalStateException e) {
            System.out.println("队列满时 add 抛出: " + e.getClass().getSimpleName());
        }

        // 第二组：失败时返回 false 或 null，因此可以用返回值控制流程，不必靠 try-catch
        System.out.println("队列满时 offer 返回: " + queue.offer("c"));
        System.out.println("peek 查看队首（不移除）: " + queue.peek());
        System.out.println("poll 取出队首: " + queue.poll());
        System.out.println("poll 之后剩下: " + queue);

        queue.clear();
        System.out.println("清空后 poll 返回: " + queue.poll());
        System.out.println("清空后 peek 返回: " + queue.peek());
        try {
            queue.remove();   // 空队列
        } catch (NoSuchElementException e) {
            System.out.println("空队列 remove 抛出: " + e.getClass().getSimpleName());
        }
        try {
            queue.element();  // 空队列
        } catch (NoSuchElementException e) {
            System.out.println("空队列 element 抛出: " + e.getClass().getSimpleName());
        }
    }

    /**
     * ② 第三组：put / take 的阻塞语义
     */
    public static void blockingBehaviour() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.put("first");
        System.out.println("容量为 1 的队列放入一个元素后，剩余容量: " + queue.remainingCapacity());

        // 队列已满，生产者的 put 会阻塞在这里，直到有人取走元素
        CountDownLatch producerStarted = new CountDownLatch(1);
        CountDownLatch produced = new CountDownLatch(1);
        Thread producer = new Thread(() -> {
            producerStarted.countDown();
            try {
                queue.put("second");
                // 这里只发信号不打印，避免子线程的输出与主线程交织、破坏输出顺序的确定性
                produced.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");
        producer.start();
        producerStarted.await();

        String first = queue.take();
        produced.await();   // 生产者的 put 已经成功返回，说明它的阻塞确实被这次 take 解除了
        System.out.println("take 取到: " + first);
        System.out.println("队列满时 put 会一直阻塞，直到有元素被 take 走");
        System.out.println("再 take 取到: " + queue.take());
        producer.join();

        // 反过来，队列为空时 take 会阻塞，直到有人放入元素
        BlockingQueue<String> empty = new ArrayBlockingQueue<>(2);
        CountDownLatch consumerStarted = new CountDownLatch(1);
        CountDownLatch consumed = new CountDownLatch(1);
        AtomicReference<String> received = new AtomicReference<>();
        Thread consumer = new Thread(() -> {
            consumerStarted.countDown();
            try {
                received.set(empty.take());
                consumed.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");
        consumer.start();
        consumerStarted.await();
        empty.put("data");
        consumed.await();
        consumer.join();
        System.out.println("空队列上 take 会一直阻塞，放入元素后取到: " + received.get());
    }

    /**
     * ③ 第四组：带超时的 offer / poll，用来在「等一会儿」与「无限期卡死」之间取折中
     */
    public static void timeoutMethods() throws InterruptedException {
        BlockingQueue<String> full = new ArrayBlockingQueue<>(1);
        full.put("only");
        long start = System.nanoTime();
        boolean offered = full.offer("another", WAIT_MILLIS, TimeUnit.MILLISECONDS);
        long offerElapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("队列满时 offer(" + WAIT_MILLIS + "ms) 的结果: " + offered);
        System.out.println("是否等满了超时时间才返回: " + (offerElapsed >= WAIT_MILLIS));

        BlockingQueue<String> empty = new ArrayBlockingQueue<>(1);
        start = System.nanoTime();
        String polled = empty.poll(WAIT_MILLIS, TimeUnit.MILLISECONDS);
        long pollElapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("空队列上 poll(" + WAIT_MILLIS + "ms) 的结果: " + polled);
        System.out.println("是否等满了超时时间才返回: " + (pollElapsed >= WAIT_MILLIS));
    }

    /**
     * ④ 有界与无界：这直接决定了系统会不会被压垮
     */
    public static void boundedVsUnbounded() {
        System.out.println("ArrayBlockingQueue 必须指定容量，此处剩余容量: "
            + new ArrayBlockingQueue<String>(5).remainingCapacity());
        // LinkedBlockingQueue 不传容量时默认是 Integer.MAX_VALUE，实际等于「无界」
        System.out.println("LinkedBlockingQueue 不指定容量时默认剩余容量: "
            + new LinkedBlockingQueue<String>().remainingCapacity());
        System.out.println("PriorityBlockingQueue 无界，剩余容量: "
            + new PriorityBlockingQueue<String>().remainingCapacity());
        System.out.println("SynchronousQueue 不存储元素，剩余容量: "
            + new SynchronousQueue<String>().remainingCapacity());

        // 这个默认值正是线上事故的常见根源：Executors.newFixedThreadPool 与 newSingleThreadExecutor
        // 内部用的就是无界的 LinkedBlockingQueue。当任务提交速度长期高于消费速度时，
        // 队列会无限堆积直到耗尽内存——这就是《阿里巴巴 Java 开发手册》禁止用 Executors
        // 创建线程池的原因。正确做法是手工 new ThreadPoolExecutor 并显式指定有界队列与拒绝策略。
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        try {
            System.out.println("Executors.newFixedThreadPool 默认队列的剩余容量: "
                + executor.getQueue().remainingCapacity());
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * ⑤ 实现选型，以及 SynchronousQueue 的「直接交接」语义
     */
    public static void implementationGuide() throws InterruptedException {
        System.out.println("常见 BlockingQueue 实现的选型对照：");
        System.out.println("  ArrayBlockingQueue    —— 数组、必须有界、一把锁（存取互斥），适合容量明确的场景");
        System.out.println("  LinkedBlockingQueue   —— 链表、可选有界、存取各一把锁，吞吐通常高于前者");
        System.out.println("  PriorityBlockingQueue —— 二叉堆、无界、按优先级出队而非 FIFO");
        System.out.println("  SynchronousQueue      —— 容量为 0，每个 put 必须等到有 take 才完成，是「直接交接」");
        System.out.println("  DelayQueue            —— 无界、元素必须实现 Delayed，到期后才能被取出，用于定时任务");
        System.out.println("  LinkedTransferQueue   —— 链表、无界，额外提供 transfer：一直等到有消费者接手为止");

        // SynchronousQueue 没有任何存储空间，它只是把生产者的手递到消费者手上
        SynchronousQueue<String> handoff = new SynchronousQueue<>();
        System.out.println("没有消费者在等时，SynchronousQueue.offer 返回: " + handoff.offer("data"));
        System.out.println("SynchronousQueue 的 size 恒为: " + handoff.size());

        AtomicReference<String> received = new AtomicReference<>();
        CountDownLatch consumed = new CountDownLatch(1);
        Thread taker = new Thread(() -> {
            try {
                received.set(handoff.take());   // 阻塞，直到有生产者直接交给它
                consumed.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "taker");
        taker.start();
        // 带超时的 offer 会等到有消费者接手为止，因此不依赖任何固定 sleep
        boolean handedOff = handoff.offer("data", HANDOFF_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        consumed.await();
        taker.join();
        System.out.println("有消费者在等时，offer 完成直接交接: " + handedOff);
        System.out.println("消费者收到的内容: " + received.get());
    }

    /**
     * 依次演示四组方法、阻塞语义、超时方法、有界性与实现选型
     */
    public static void demo() throws InterruptedException {
        fourMethodGroups();
        blockingBehaviour();
        timeoutMethods();
        boundedVsUnbounded();
        implementationGuide();
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}

// Output:
// add(a): true
// add(b): true
// 队列满时 add 抛出: IllegalStateException
// 队列满时 offer 返回: false
// peek 查看队首（不移除）: a
// poll 取出队首: a
// poll 之后剩下: [b]
// 清空后 poll 返回: null
// 清空后 peek 返回: null
// 空队列 remove 抛出: NoSuchElementException
// 空队列 element 抛出: NoSuchElementException
// 容量为 1 的队列放入一个元素后，剩余容量: 0
// take 取到: first
// 队列满时 put 会一直阻塞，直到有元素被 take 走
// 再 take 取到: second
// 空队列上 take 会一直阻塞，放入元素后取到: data
// 队列满时 offer(50ms) 的结果: false
// 是否等满了超时时间才返回: true
// 空队列上 poll(50ms) 的结果: null
// 是否等满了超时时间才返回: true
// ArrayBlockingQueue 必须指定容量，此处剩余容量: 5
// LinkedBlockingQueue 不指定容量时默认剩余容量: 2147483647
// PriorityBlockingQueue 无界，剩余容量: 2147483647
// SynchronousQueue 不存储元素，剩余容量: 0
// Executors.newFixedThreadPool 默认队列的剩余容量: 2147483647
// 常见 BlockingQueue 实现的选型对照：
//   ArrayBlockingQueue    —— 数组、必须有界、一把锁（存取互斥），适合容量明确的场景
//   LinkedBlockingQueue   —— 链表、可选有界、存取各一把锁，吞吐通常高于前者
//   PriorityBlockingQueue —— 二叉堆、无界、按优先级出队而非 FIFO
//   SynchronousQueue      —— 容量为 0，每个 put 必须等到有 take 才完成，是「直接交接」
//   DelayQueue            —— 无界、元素必须实现 Delayed，到期后才能被取出，用于定时任务
//   LinkedTransferQueue   —— 链表、无界，额外提供 transfer：一直等到有消费者接手为止
// 没有消费者在等时，SynchronousQueue.offer 返回: false
// SynchronousQueue 的 size 恒为: 0
// 有消费者在等时，offer 完成直接交接: true
// 消费者收到的内容: data
