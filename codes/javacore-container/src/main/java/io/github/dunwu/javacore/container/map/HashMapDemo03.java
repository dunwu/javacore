package io.github.dunwu.javacore.container.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 示例：keySet 获取 HashMap 全部 key 并遍历（注意：HashMap 遍历顺序不保证）。
 */
public class HashMapDemo03 {

    /** 演示通过 keySet 遍历全部 key。 */
    public static void demo() {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        Set<String> keys = map.keySet();    // 得到全部的key
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            System.out.print(str + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
