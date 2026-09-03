package io.github.dunwu.javacore.container.queue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * 示例：ArrayDeque 双端队列。
 * <p>
 * ArrayDeque 用<b>循环数组</b>实现，两端都能 O(1) 进出，因此它同时能扮演三种角色，
 * 而且每一种都比 JDK 里那个「专用类」更值得推荐：
 * <ul>
 *     <li><b>当栈用</b>（{@code push} / {@code pop}）—— 优于 {@link java.util.Stack}。
 *     Stack 继承自 Vector，每个方法都加了 {@code synchronized}，单线程下白白付出锁开销；
 *     它还暴露了 {@code get(index)} 这类随机访问，允许从中间插入元素，直接破坏了栈的语义。
 *     Stack 是 JDK 1.0 的遗留类，官方文档已明确建议改用 Deque</li>
 *     <li><b>当队列用</b>（{@code offer} / {@code poll}）—— 优于 {@link java.util.LinkedList}。
 *     数组实现内存连续、CPU 缓存友好，且不需要为每个元素分配 Node 对象，GC 压力明显更小</li>
 *     <li><b>当双端队列用</b>（{@code offerFirst} / {@code offerLast} 等）—— 滑动窗口、
 *     单调队列、工作窃取（{@code ForkJoinPool} 的任务队列就是双端队列）等算法的基础结构</li>
 * </ul>
 * 其他特性：<b>无界</b>（数组满了自动扩容成两倍，对外完全透明）、<b>不允许 null</b>、
 * <b>非线程安全</b>（并发场景用 {@link java.util.concurrent.ConcurrentLinkedDeque} 或
 * {@link java.util.concurrent.LinkedBlockingDeque}）。
 * <p>
 * 方法命名规律：{@code xxxFirst} 操作队首，{@code xxxLast} 操作队尾；
 * {@code add} / {@code remove} / {@code get} 系列失败时<b>抛异常</b>，
 * {@code offer} / {@code poll} / {@code peek} 系列失败时<b>返回 false 或 null</b>。
 * 生产代码优先用后者，避免用异常控制流程。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayDequeDemo {

    /**
     * ① 双端进出：队首与队尾都能 O(1) 操作
     */
    public static void bothEnds() {
        Deque<String> deque = new ArrayDeque<>();
        deque.offerFirst("b");
        deque.offerFirst("a");   // 插到队首，所以排在 b 前面
        deque.offerLast("c");    // 插到队尾
        System.out.println("依次 offerFirst(b)、offerFirst(a)、offerLast(c) 后: " + deque);
        System.out.println("peekFirst（不移除）: " + deque.peekFirst());
        System.out.println("peekLast（不移除）: " + deque.peekLast());
        System.out.println("peek 之后大小不变: " + deque.size());
        System.out.println("pollFirst: " + deque.pollFirst());
        System.out.println("pollLast: " + deque.pollLast());
        System.out.println("两端各取出一个后剩下: " + deque);
    }

    /**
     * ② 当栈用：push / pop 都作用在队首，因此是后进先出
     */
    public static void asStack() {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        // push 等价于 addFirst，所以后压入的排在最前面
        System.out.println("依次 push 1、2、3 后: " + stack);
        System.out.println("peek（看栈顶不弹出）: " + stack.peek());
        System.out.println("pop: " + stack.pop());
        System.out.println("pop: " + stack.pop());
        System.out.println("弹出两次后剩下: " + stack);

        // 括号匹配是栈最典型的应用：遇到左括号压栈，遇到右括号弹栈比对
        System.out.println("表达式 ((a+b)*[c-d]) 的括号是否匹配: " + isBalanced("((a+b)*[c-d])"));
        // 这个串把 ] 写成了 )，属于「交叉闭合」，弹栈时就会发现对不上
        System.out.println("表达式 ((a+b)*[c-d) 的括号是否匹配: " + isBalanced("((a+b)*[c-d)"));
    }

    /**
     * ③ 当队列用：offer / poll 作用在两端，因此是先进先出
     */
    public static void asQueue() {
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("a");
        queue.offer("b");
        // offer 等价于 offerLast，poll 等价于 pollFirst，一头进另一头出，正是 FIFO
        System.out.println("依次 offer a、b 后: " + queue);
        System.out.println("poll: " + queue.poll());
        System.out.println("取出一个后剩下: " + queue);

        // 用队列实现广度优先遍历：把待访问的节点排在队尾，逐个取出并展开
        Deque<Integer> bfs = new ArrayDeque<>();
        bfs.offer(1);
        List<Integer> visited = new ArrayList<>();
        while (!bfs.isEmpty()) {
            int node = bfs.poll();
            visited.add(node);
            // 这里用「子节点 = 父节点 * 2 与 父节点 * 2 + 1」模拟一棵二叉树，只展开到 4 层
            if (node * 2 <= 8) {
                bfs.offer(node * 2);
                bfs.offer(node * 2 + 1);
            }
        }
        System.out.println("层序遍历结果: " + visited);
    }

    /**
     * ④ 边界：不允许 null，因为 null 被用作「队列为空」的返回信号
     */
    public static void nullNotAllowed() {
        Deque<String> deque = new ArrayDeque<>();
        try {
            deque.offer(null);
        } catch (NullPointerException e) {
            System.out.println("offer(null) 抛出: " + e.getClass().getSimpleName());
        }
        try {
            deque.push(null);
        } catch (NullPointerException e) {
            System.out.println("push(null) 抛出: " + e.getClass().getSimpleName());
        }
        // 正因为 poll / peek 用 null 表示「没有元素」，一旦允许存入 null，
        // 调用方就再也分不清「取到了一个 null 元素」和「队列已经空了」
        System.out.println("空队列 pollFirst 返回: " + deque.pollFirst());
        System.out.println("空队列 peekFirst 返回: " + deque.peekFirst());
        System.out.println("空队列 pop 会抛异常: " + throwsOnEmptyPop(deque));
    }

    /**
     * ⑤ 遍历与扩容：正反两个方向都能遍历，扩容对外完全透明
     */
    public static void iterationAndGrowth() {
        // 构造参数是内部数组的初始长度（会被向上取整到 2 的幂），不是容量上限
        Deque<String> deque = new ArrayDeque<>(2);
        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");   // 此处触发扩容，但调用方毫无感知
        System.out.println("初始长度给 2，装入 3 个元素后: " + deque);

        // 正向迭代：从队首到队尾，这与 LinkedList 等所有 Deque 的约定一致
        List<String> forward = new ArrayList<>();
        deque.iterator().forEachRemaining(forward::add);
        System.out.println("iterator 正向遍历: " + forward);

        // 反向迭代：descendingIterator 从队尾到队首
        List<String> backward = new ArrayList<>();
        deque.descendingIterator().forEachRemaining(backward::add);
        System.out.println("descendingIterator 反向遍历: " + backward);

        // 扩容后元素仍然按插入顺序排列，说明循环数组的「回绕」被正确屏蔽了
        System.out.println("扩容没有打乱顺序: " + forward.equals(List.of("a", "b", "c")));
    }

    /**
     * 依次演示五个侧面
     */
    public static void demo() {
        bothEnds();
        asStack();
        asQueue();
        nullNotAllowed();
        iterationAndGrowth();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 用栈检查括号是否成对匹配
     *
     * @param text 待检查的表达式
     * @return 括号完全匹配返回 true
     */
    private static boolean isBalanced(String text) {
        Deque<Character> stack = new ArrayDeque<>();
        String open = "([{";
        String close = ")]}";
        for (char c : text.toCharArray()) {
            int openIndex = open.indexOf(c);
            if (openIndex >= 0) {
                stack.push(c);
                continue;
            }
            int closeIndex = close.indexOf(c);
            if (closeIndex >= 0) {
                // 栈空说明右括号多余；栈顶与右括号不配对说明交叉了
                if (stack.isEmpty() || stack.pop() != open.charAt(closeIndex)) {
                    return false;
                }
            }
        }
        // 遍历结束后栈必须为空，否则说明有左括号没被闭合
        return stack.isEmpty();
    }

    /**
     * 空栈上调用 pop 会抛 {@link java.util.NoSuchElementException}，
     * 这一点与遗留的 {@code Stack.pop()} 抛 {@code EmptyStackException} 不同
     */
    private static String throwsOnEmptyPop(Deque<String> deque) {
        try {
            deque.pop();
            return "没有抛异常";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

}

// Output:
// 依次 offerFirst(b)、offerFirst(a)、offerLast(c) 后: [a, b, c]
// peekFirst（不移除）: a
// peekLast（不移除）: c
// peek 之后大小不变: 3
// pollFirst: a
// pollLast: c
// 两端各取出一个后剩下: [b]
// 依次 push 1、2、3 后: [3, 2, 1]
// peek（看栈顶不弹出）: 3
// pop: 3
// pop: 2
// 弹出两次后剩下: [1]
// 表达式 ((a+b)*[c-d]) 的括号是否匹配: true
// 表达式 ((a+b)*[c-d) 的括号是否匹配: false
// 依次 offer a、b 后: [a, b]
// poll: a
// 取出一个后剩下: [b]
// 层序遍历结果: [1, 2, 3, 4, 5, 6, 7, 8, 9]
// offer(null) 抛出: NullPointerException
// push(null) 抛出: NullPointerException
// 空队列 pollFirst 返回: null
// 空队列 peekFirst 返回: null
// 空队列 pop 会抛异常: NoSuchElementException
// 初始长度给 2，装入 3 个元素后: [a, b, c]
// iterator 正向遍历: [a, b, c]
// descendingIterator 反向遍历: [c, b, a]
// 扩容没有打乱顺序: true
