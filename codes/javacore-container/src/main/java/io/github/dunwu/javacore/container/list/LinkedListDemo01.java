package io.github.dunwu.javacore.container.list;

import java.util.LinkedList;
import java.util.List;

/**
 * 示例：LinkedList 作为 List 的基本操作 —— 增删元素与三种遍历方式（下标、增强 for、Lambda）。
 */
public class LinkedListDemo01 {

    /** 演示 LinkedList 的添加、删除与三种遍历方式。 */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
