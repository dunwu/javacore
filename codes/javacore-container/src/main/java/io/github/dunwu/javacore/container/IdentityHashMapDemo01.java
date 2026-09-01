package io.github.dunwu.javacore.container;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 示例：HashMap 中内容相同（equals 相等）的 Person 会被视为同一个 key，后者覆盖前者。
 * <p>
 * 与 IdentityHashMapDemo02 对比：IdentityHashMap 只按引用地址判断 key 是否相同。
 */
public class IdentityHashMapDemo01 {

    /** 演示 HashMap 对内容相同的 key 去重。 */
    public static void demo() {
        Map<Person, String> map = null;    // 声明Map对象
        map = new HashMap<Person, String>();
        map.put(new Person("张三", 30), "zhangsan_1");    // 加入内容
        map.put(new Person("张三", 30), "zhangsan_2");    // 加入内容
        map.put(new Person("李四", 31), "lisi");    // 加入内容
        Set<Map.Entry<Person, String>> allSet = null;    // 准备使用Set接收全部内容
        allSet = map.entrySet();
        Iterator<Map.Entry<Person, String>> iter = null;
        iter = allSet.iterator();
        while (iter.hasNext()) {
            Map.Entry<Person, String> me = iter.next();
            System.out.println(me.getKey() + " --> " + me.getValue());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
