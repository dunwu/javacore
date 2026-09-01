package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例：key 为自定义对象 —— 用同一对象引用（同一地址）作 key 取值，必然命中。
 */
public class HashMapDemo07 {

    /** 演示用同一对象引用作为 key 取值。 */
    public static void demo() {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        Person per = new Person("张三", 30);
        map.put(per, "zhangsan");    // 增加内容
        System.out.println(map.get(per));
    }

    public static void main(String[] args) {
        demo();
    }

}
