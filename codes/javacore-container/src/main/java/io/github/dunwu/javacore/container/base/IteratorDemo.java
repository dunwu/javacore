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

    public static void main(String[] args) {
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

}
