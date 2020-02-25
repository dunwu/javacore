package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo07 {

    public static void main(String[] args) {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        Person per = new Person("张三", 30);
        map.put(per, "zhangsan");    // 增加内容
        System.out.println(map.get(per));
    }

}
