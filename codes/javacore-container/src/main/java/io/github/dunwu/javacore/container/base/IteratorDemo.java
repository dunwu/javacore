package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 迭代器 {@link Iterator}示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-29
 */
public class IteratorDemo {

    /** 演示用迭代器遍历 List。 */
    public static void demo() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        // 获取迭代器
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
