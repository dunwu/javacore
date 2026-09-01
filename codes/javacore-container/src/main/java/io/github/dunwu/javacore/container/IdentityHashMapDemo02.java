package io.github.dunwu.javacore.container;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 示例：IdentityHashMap 只按引用地址判断 key 是否相同，内容相同但地址不同的对象是不同 key。
 * <p>
 * 与 IdentityHashMapDemo01 对比：HashMap 会按 equals 去重。
 */
public class IdentityHashMapDemo02 {

    /** 演示 IdentityHashMap 保留内容相同但地址不同的 key。 */
    public static void demo() {
        Map<Person, String> map = null;    // 声明Map对象
        map = new IdentityHashMap<Person, String>();
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
