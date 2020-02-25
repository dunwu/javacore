package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayListDemo01 {

    public static void main(String[] args) {

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

}
