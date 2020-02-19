package io.github.dunwu.javacore.container;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class IdentityHashMapDemo02 {

    public static void main(String[] args) {
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

}
