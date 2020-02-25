package io.github.dunwu.javacore.container.list;

import java.util.Deque;
import java.util.LinkedList;

public class LinkedListDemo02 {

    public static void main(String[] args) {
        Deque<String> list = new LinkedList<String>();
        list.add("A"); // 添加元素
        list.add("B"); // 添加元素
        list.add("C"); // 添加元素
        list.addFirst("X"); // 在头部添加元素
        list.addLast("Y"); // 在尾部添加元素
        System.out.println("初始化 LinkedList：" + list);
        System.out.println("1-1、element()方法找到表头：" + list.element());
        System.out.println("1-2、找完之后的链表的内容：" + list);
        System.out.println("2-1、peek()方法找到表头：" + list.peek());
        System.out.println("2-2、找完之后的链表的内容：" + list);
        System.out.println("3-1、poll()方法找到表头：" + list.poll());
        System.out.println("3-2、找完之后的链表的内容：" + list);

        System.out.print("以FIFO的方式输出：");
        for (int i = 0; i <= list.size() + 1; i++) {
            System.out.print(list.poll() + "、");
        }
    }

}
