package io.github.dunwu.javacore.container.list;

import java.util.LinkedList;
import java.util.List;

public class LinkedListDemo01 {

    public static void main(String[] args) {
        List<String> list = new LinkedList<String>();
        // 增加元素
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("初始化链表：" + list);

        System.out.println("第一次遍历");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        System.out.println("第二次遍历");
        for (String element : list) {
            System.out.print(element + " ");
        }
        System.out.println();

        // JDK8 Lambda 表达式遍历
        System.out.println("第三次遍历");
        list.forEach(element -> {
            System.out.print(element + " ");
        });
        System.out.println();

        list.remove("C");
        System.out.println("链表：" + list);
    }

}
