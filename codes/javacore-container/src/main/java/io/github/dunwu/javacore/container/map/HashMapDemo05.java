package io.github.dunwu.javacore.container.map;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例：value 为自定义对象 —— key 用 String，get 必然取到值。
 * 与 HashMapDemo06/07 对比：演示 key 类型对查找行为的影响。
 */
public class HashMapDemo05 {

    /** 演示 String 作为 key 时按 key 取值。 */
    public static void demo() {
        Map<String, Person> map = null;
        map = new HashMap<String, Person>();
        map.put("zhangsan", new Person("张三", 30));    // 增加内容
        System.out.println(map.get("zhangsan"));
    }

    public static void main(String[] args) {
        demo();
    }

}
