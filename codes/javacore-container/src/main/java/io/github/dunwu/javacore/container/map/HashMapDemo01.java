package io.github.dunwu.javacore.container.map;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo01 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        String val = map.get("mldn");    // 根据key取出值
        System.out.println("取出的内容是：" + val);
    }

}
