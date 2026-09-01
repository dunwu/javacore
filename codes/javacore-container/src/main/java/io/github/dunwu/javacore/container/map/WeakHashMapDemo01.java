package io.github.dunwu.javacore.container.map;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 示例：WeakHashMap 的 key 仅持有弱引用 —— 当 key 在外部无强引用时，GC 后键值对会被自动回收。
 * 本例的 key 是字符串常量池中的字面量（始终有强引用），故 GC 后内容仍在；
 * 典型应用场景是以对象为 key 的缓存。
 */
public class WeakHashMapDemo01 {

    /** 演示 WeakHashMap 存入与输出。 */
    public static void demo() {
        Map<String, String> map = null; // 声明Map对象，其中key和value的类型为String
        map = new WeakHashMap<String, String>();
        map.put("mldn", "www.mldn.cn");
        map.put("zhinangtuan", "www.zhinangtuan.net.cn");
        map.put("mldnjava", "www.mldnjava.cn");
        System.gc();    // 强制性进行垃圾的收集操作
        map.put("lxh", "lixinghua");
        System.out.println(map);
    }

    public static void main(String[] args) {
        demo();
    }

}
