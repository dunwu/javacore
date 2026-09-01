package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 示例：在迭代中安全删除元素——必须用 iterator.remove()，直接调 list.remove() 会触发 fail-fast。
 */
public class IteratorDemo2 {

    /** 演示迭代时用 iterator.remove() 删除指定元素。 */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
