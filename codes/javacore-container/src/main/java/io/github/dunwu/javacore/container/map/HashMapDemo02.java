package io.github.dunwu.javacore.container.map;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo02 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        if (map.containsKey("mldn")) {    // 判断key是否存在
            System.out.println("搜索的key存在！");
        } else {
            System.out.println("搜索的key不存在！");
        }
        if (map.containsValue("www.mldn.cn")) {    // 判断value是否存在
            System.out.println("搜索的value存在！");
        } else {
            System.out.println("搜索的value不存在！");
        }
    }

}
