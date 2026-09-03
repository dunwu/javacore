package io.github.dunwu.javacore.container.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 示例：PriorityQueue 优先级队列。
 * <p>
 * PriorityQueue 与普通队列（FIFO）的根本区别：出队顺序由<b>元素的优先级</b>决定，而不是入队顺序。
 * 它的底层是<b>数组实现的二叉堆</b>（默认小顶堆），因此：
 * <ul>
 *     <li>{@code offer} / {@code poll} 是 O(log n)——需要「上浮」或「下沉」来维持堆性质</li>
 *     <li>{@code peek} 是 O(1)——堆顶就在数组下标 0 处</li>
 *     <li>{@code remove(Object)} 是 O(n)——得先线性找到它</li>
 * </ul>
 * <b>最容易踩的坑</b>：堆只保证「父节点不大于子节点」，<b>并不保证整个数组有序</b>。
 * 因此 {@code iterator()}、{@code toString()}、{@code toArray()} 给出的都是堆的内部存储顺序，
 * 与优先级无关。想按优先级顺序拿到全部元素，<b>只能不断 {@code poll()}</b>，见
 * {@link #iterationOrderIsNotPriorityOrder()}。
 * <p>
 * 其他特性：<b>无界</b>（构造参数只是内部数组的初始长度，装满了自动扩容）、<b>不允许 null</b>、
 * <b>非线程安全</b>（并发场景要用 {@link java.util.concurrent.PriorityBlockingQueue}）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class PriorityQueueDemo {

    /**
     * ① 默认小顶堆：poll 依次取出最小值
     */
    public static void naturalOrder() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        // 入队顺序刻意是乱的
        queue.offer(5);
        queue.offer(1);
        queue.offer(3);
        System.out.println("入队顺序: [5, 1, 3]");
        System.out.println("peek 只看队首、不移除: " + queue.peek());
        System.out.println("peek 之后队列大小不变: " + queue.size());
        System.out.println("poll 依次取出: " + pollAll(queue));
        System.out.println("取空后 peek 返回: " + queue.peek());
        System.out.println("取空后 poll 返回: " + queue.poll());
    }

    /**
     * ② 关键坑：iterator / toString 的顺序是堆的内部存储顺序，不是优先级顺序
     */
    public static void iterationOrderIsNotPriorityOrder() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        int[] values = { 1, 3, 5, 7, 9, 2 };
        for (int value : values) {
            queue.offer(value);
        }
        // 最后入队的 2 会把堆里的 5 顶下去，于是存储顺序变成 [1, 3, 2, 7, 9, 5]——
        // 它既不是入队顺序，也不是有序序列，这正是「堆只保证父子关系」的直接体现
        String storageOrder = queue.toString();
        List<Integer> pollOrder = pollAll(queue);
        System.out.println("入队顺序: " + Arrays.toString(values));
        System.out.println("toString / iterator 的顺序: " + storageOrder);
        System.out.println("poll 出队顺序: " + pollOrder);
        System.out.println("遍历顺序与出队顺序是否一致: " + storageOrder.equals(pollOrder.toString()));
    }

    /**
     * ③ 自定义 Comparator：把小顶堆翻转成大顶堆
     */
    public static void customComparator() {
        // 传 Comparator.reverseOrder() 即可，不需要自己写比较逻辑
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        for (int value : new int[] { 5, 1, 3 }) {
            maxHeap.offer(value);
        }
        System.out.println("入队顺序: [5, 1, 3]");
        System.out.println("大顶堆 poll 顺序: " + pollAll(maxHeap));

        // 也可以按「长度」这类派生属性排序，而不必让元素本身实现 Comparable
        PriorityQueue<String> byLength = new PriorityQueue<>(Comparator.comparingInt(String::length));
        byLength.offer("Java");
        byLength.offer("C");
        byLength.offer("Python");
        System.out.println("按字符串长度升序 poll: " + pollAll(byLength));
    }

    /**
     * ④ 自定义对象：让元素实现 Comparable，或直接传 Comparator
     */
    public static void customObject() {
        // Task 实现了 Comparable，因此可以用无参构造
        PriorityQueue<Task> byComparable = new PriorityQueue<>();
        byComparable.offer(new Task("写文档", 3));
        byComparable.offer(new Task("修线上故障", 1));
        byComparable.offer(new Task("代码评审", 2));
        System.out.println("按 Comparable 出队: " + pollNames(byComparable));

        // 同一个类，换一个 Comparator 就能得到完全不同的出队顺序，
        // 这比「为了改排序而修改 Task 的 compareTo」灵活得多
        PriorityQueue<Task> byPriorityDesc = new PriorityQueue<>(
            Comparator.comparingInt(Task::priority).reversed());
        byPriorityDesc.offer(new Task("写文档", 3));
        byPriorityDesc.offer(new Task("修线上故障", 1));
        byPriorityDesc.offer(new Task("代码评审", 2));
        System.out.println("按 Comparator 倒序出队: " + pollNames(byPriorityDesc));
    }

    /**
     * ⑤ 经典应用：用容量为 K 的小顶堆求 Top-K 最大值
     */
    public static void topK() {
        int[] data = { 3, 9, 1, 7, 5, 8, 2 };
        int k = 3;
        // 小顶堆的堆顶是「当前已选中的 K 个最大值里最小的那一个」，
        // 所以新元素只要比堆顶大，就有资格挤掉堆顶。整个过程只占 O(K) 内存、O(n log K) 时间，
        // 比「全量排序后取前 K 个」更适合海量数据流
        PriorityQueue<Integer> heap = new PriorityQueue<>(k);
        for (int value : data) {
            if (heap.size() < k) {
                heap.offer(value);
            } else if (value > heap.peek()) {
                heap.poll();
                heap.offer(value);
            }
        }
        List<Integer> topK = pollAll(heap);
        Collections.sort(topK);
        System.out.println("数据: " + Arrays.toString(data));
        System.out.println("最大的 " + k + " 个: " + topK);
    }

    /**
     * ⑥ 边界：不允许 null；无界意味着 offer 永远不会失败
     */
    public static void boundaries() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        try {
            queue.offer(null);
        } catch (NullPointerException e) {
            // 与 ArrayDeque 同理：poll 用 null 表示「队列已空」，若允许存 null 就无法区分两种情况
            System.out.println("offer(null) 抛出: " + e.getClass().getSimpleName());
        }

        // 构造参数只是内部数组的初始长度，不是容量上限。装满会自动扩容，
        // 因此 offer 的返回值恒为 true——这一点与「有界」的 ArrayBlockingQueue 完全不同
        PriorityQueue<Integer> unbounded = new PriorityQueue<>(1);
        boolean allAccepted = true;
        for (int i = 0; i < 100; i++) {
            allAccepted &= unbounded.offer(i);
        }
        System.out.println("初始容量给 1，装入 100 个元素全部成功: " + allAccepted);
        System.out.println("实际元素个数: " + unbounded.size());
        System.out.println("PriorityQueue 非线程安全，并发场景应改用 PriorityBlockingQueue");
    }

    /**
     * 依次演示六个侧面
     */
    public static void demo() {
        naturalOrder();
        iterationOrderIsNotPriorityOrder();
        customComparator();
        customObject();
        topK();
        boundaries();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 不断 poll 直到取空，从而得到「按优先级排列」的结果。
     * 这是从 PriorityQueue 中按序取出全部元素的<b>唯一</b>正确方式
     */
    private static <T> List<T> pollAll(Queue<T> queue) {
        List<T> result = new ArrayList<>();
        T item;
        while ((item = queue.poll()) != null) {
            result.add(item);
        }
        return result;
    }

    /**
     * 按优先级出队并只取名称，便于阅读
     */
    private static List<String> pollNames(PriorityQueue<Task> queue) {
        List<String> names = new ArrayList<>();
        Task task;
        while ((task = queue.poll()) != null) {
            names.add(task.name());
        }
        return names;
    }

    /**
     * 待排度的任务。数字越小优先级越高
     */
    record Task(String name, int priority) implements Comparable<Task> {

        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.priority, other.priority);
        }

    }

}

// Output:
// 入队顺序: [5, 1, 3]
// peek 只看队首、不移除: 1
// peek 之后队列大小不变: 3
// poll 依次取出: [1, 3, 5]
// 取空后 peek 返回: null
// 取空后 poll 返回: null
// 入队顺序: [1, 3, 5, 7, 9, 2]
// toString / iterator 的顺序: [1, 3, 2, 7, 9, 5]
// poll 出队顺序: [1, 2, 3, 5, 7, 9]
// 遍历顺序与出队顺序是否一致: false
// 入队顺序: [5, 1, 3]
// 大顶堆 poll 顺序: [5, 3, 1]
// 按字符串长度升序 poll: [C, Java, Python]
// 按 Comparable 出队: [修线上故障, 代码评审, 写文档]
// 按 Comparator 倒序出队: [写文档, 代码评审, 修线上故障]
// 数据: [3, 9, 1, 7, 5, 8, 2]
// 最大的 3 个: [7, 8, 9]
// offer(null) 抛出: NullPointerException
// 初始容量给 1，装入 100 个元素全部成功: true
// 实际元素个数: 100
// PriorityQueue 非线程安全，并发场景应改用 PriorityBlockingQueue
