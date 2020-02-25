package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorDemo2 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println("执行前：" + list);

        Iterator<Integer> iterator = list.iterator();
        // 判断迭代器还有后继节点
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value == 2) {
                // 删除元素
                iterator.remove();
            } else {
                System.out.println(value);    // 输出内容
            }
        }
        System.out.println("执行后：" + list);
    }

}
