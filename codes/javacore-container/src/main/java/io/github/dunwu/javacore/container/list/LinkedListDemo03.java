package io.github.dunwu.javacore.container.list;

import java.util.LinkedList;

public class LinkedListDemo03 {

    public static void main(String[] args) {
        LinkedList<String> link = new LinkedList<String>();
        link.add("A");    // 增加元素
        link.add("B");    // 增加元素
        link.add("C");    // 增加元素
        System.out.print("以FIFO的方式输出：");
        for (int i = 0; i <= link.size() + 1; i++) {
            System.out.print(link.poll() + "、");
        }
    }

}
