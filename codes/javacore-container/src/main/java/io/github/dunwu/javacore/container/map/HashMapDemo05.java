package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo05 {

    public static void main(String[] args) {
        Map<String, Person> map = null;
        map = new HashMap<String, Person>();
        map.put("zhangsan", new Person("张三", 30));    // 增加内容
        System.out.println(map.get("zhangsan"));
    }

}
