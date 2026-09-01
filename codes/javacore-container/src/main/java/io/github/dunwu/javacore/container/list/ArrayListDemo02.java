package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例：ArrayList 删除操作 —— 按位置删除和按内容删除。
 */
public class ArrayListDemo02 {

    /** 演示 ArrayList 的删除操作。 */
    public static void demo() {
        List<String> list = new ArrayList<String>();
        list.add("Hello");    // 此方法由Collection接口而来
        list.add(0, "World");    // 在第一个位置上添加新的内容
        list.add("MLDN");    // 向Collection中加入内容
        list.add("www.mldn.cn");
        list.remove(0); // 删除第一个元素，指定删除的位置
        list.remove("Hello");    // 此方法由Collection接口继承而来
        System.out.println(list);
    }

    public static void main(String[] args) {
        demo();
    }

}
