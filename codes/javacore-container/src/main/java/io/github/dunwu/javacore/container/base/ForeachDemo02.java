package io.github.dunwu.javacore.container.base;

import java.util.HashMap;
import java.util.Map;

public class ForeachDemo02 {

    public static void main(String[] args) {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new HashMap<String, String>();
        map.put("mldn", "www.mldn.cn");    // 增加内容
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
        map.put("mldnjava", "www.mldnjava.cn");    // 增加内容
        for (Map.Entry<String, String> me : map.entrySet()) {
            System.out.println(me.getKey() + " --> " + me.getValue());
        }
    }

}
