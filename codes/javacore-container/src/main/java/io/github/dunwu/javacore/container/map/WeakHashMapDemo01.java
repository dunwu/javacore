package io.github.dunwu.javacore.container.map;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashMapDemo01 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new WeakHashMap<String, String>();
        map.put("mldn", "www.mldn.cn");
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");
        map.put("mldnjava", "www.mldnjava.cn");
        System.gc();    // 强制性进行垃圾的收集操作
        map.put("lxh", "lixinghua");
        System.out.println(map);
    }

}
