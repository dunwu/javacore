package io.github.dunwu.javacore.container.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapDemo04 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        Collection<String> values = map.values();    // 得到全部的value
        Iterator<String> iter = values.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            System.out.print(str + "、");
        }
    }

}
