package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例：key 为自定义对象 —— Person 正确重写了 equals/hashCode，
 * 所以即使 new 一个内容相同的新对象作 key，也能取到值。
 */
public class HashMapDemo06 {

    /** 演示内容相同的不同对象可命中 HashMap 的 key。 */
    public static void demo() {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        map.put(new Person("张三", 30), "zhangsan");    // 增加内容
        System.out.println(map.get(new Person("张三", 30)));
    }

    public static void main(String[] args) {
        demo();
    }

}
