package io.github.dunwu.javacore.container.queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 示例：用 LinkedList 实现 Queue 队列 —— offer 入队、poll 出队、element/peek 查看队首；
 * 区别：element 空队列时抛异常，peek 返回 null；add/remove 失败时抛异常（不推荐）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-02-21
 */
public class LinkedListQueueDemo {

    /** 演示队列的入队、出队与查看队首元素。 */
    public static void demo() {
        //add()和remove()方法在失败的时候会抛出异常(不推荐)
        Queue<String> queue = new LinkedList<>();

        queue.offer("a"); // 入队
        queue.offer("b"); // 入队
        queue.offer("c"); // 入队
        for (String q : queue) {
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("poll=" + queue.poll()); // 出队
        for (String q : queue) {
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("element=" + queue.element()); //返回第一个元素
        for (String q : queue) {
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("peek=" + queue.peek()); //返回第一个元素
        for (String q : queue) {
            System.out.println(q);
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
