package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 示例：ArrayList 基本操作 —— add、addAll、在指定位置插入元素或集合。
 */
public class ArrayListDemo01 {

    /** 演示 ArrayList 的添加与批量添加操作。 */
    public static void demo() {

        // 指定一个集合
        Collection<String> collection = new ArrayList<String>();
        collection.add("ABC");
        collection.add("abc");

        // 指定操作的泛型为 String
        List<String> list = new ArrayList<String>();
        list.add("world");
        list.add(0, "hello");
        System.out.println(list);

        list.addAll(collection);
        list.addAll(0, collection);
        System.out.println(list);
    }

    public static void main(String[] args) {
        demo();
    }

}
