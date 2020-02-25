package io.github.dunwu.javacore.container.queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-02-21
 */
public class LinkedListQueueDemo {

    public static void main(String[] args) {
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

}
