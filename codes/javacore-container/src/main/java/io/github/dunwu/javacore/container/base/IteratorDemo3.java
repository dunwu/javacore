package io.github.dunwu.javacore.container.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 示例：用迭代器遍历 Map 的 entrySet。
 */
public class IteratorDemo3 {

    /** 演示迭代器遍历 Map。 */
    public static void demo() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "A");    // 增加内容
        map.put(2, "B");    // 增加内容
        map.put(3, "C");    // 增加内容
        Set<Map.Entry<Integer, String>> set = map.entrySet();
        Iterator<Map.Entry<Integer, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> me = iterator.next();
            System.out.println(me.getKey() + " -> " + me.getValue());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
