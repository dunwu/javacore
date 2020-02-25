package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo06 {

    public static void main(String[] args) {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        map.put(new Person("张三", 30), "zhangsan");    // 增加内容
        System.out.println(map.get(new Person("张三", 30)));
    }

}
