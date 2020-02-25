package io.github.dunwu.javacore.container.map;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TreeMapDemo01 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new TreeMap<String, String>();
        map.put("A、mldn", "www.mldn.cn");    // 增加内容
        map.put("C、zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("B、mldnjava", "www.mldnjava.cn");    // 增加内容
        Set<String> keys = map.keySet();    // 得到全部的key
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            System.out.println(str + " --> " + map.get(str)); // 取出内容
        }
    }

}
