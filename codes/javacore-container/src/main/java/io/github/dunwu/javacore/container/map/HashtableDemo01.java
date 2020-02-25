package io.github.dunwu.javacore.container.map;

import java.util.*;

public class HashtableDemo01 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        System.out.print("全部的key：");
        Set<String> keys = map.keySet();    // 得到全部的key
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            System.out.print(str + "、");
        }
        System.out.print("\n全部的value：");
        Collection<String> values = map.values();    // 得到全部的value
        Iterator<String> iter2 = values.iterator();
        while (iter2.hasNext()) {
            String str = iter2.next();
            System.out.print(str + "、");
        }
    }

}
