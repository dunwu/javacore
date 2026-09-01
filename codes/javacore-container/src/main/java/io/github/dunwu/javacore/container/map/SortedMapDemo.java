package io.github.dunwu.javacore.container.map;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 示例：SortedMap（TreeMap 实现）—— 按键自然序排序，支持 firstKey、lastKey、headMap、tailMap、subMap 范围操作。
 */
public class SortedMapDemo {

    /** 演示 SortedMap 的排序与范围查询操作。 */
    public static void demo() {
        SortedMap<String, String> map = null;
        map = new TreeMap<String, String>();    // 通过子类实例化接口对象
        map.put("D、jiangker", "http://www.jiangker.com/");
        map.put("A、mldn", "www.mldn.cn");
        map.put("C、zhinangtuan", "www.zhinangtuan.net.cn");
        map.put("B、mldnjava", "www.mldnjava.cn");
        System.out.print("第一个元素的内容的key：" + map.firstKey());
        System.out.println("：对应的值：" + map.get(map.firstKey()));
        System.out.print("最后一个元素的内容的key：" + map.lastKey());
        System.out.println("：对应的值：" + map.get(map.lastKey()));
        System.out.println("返回小于指定范围的集合：");
        for (Map.Entry<String, String> me : map.headMap("B、mldnjava").entrySet()) {
            System.out.println("\t|- " + me.getKey() + " --> " + me.getValue());
        }
        System.out.println("返回大于指定范围的集合：");
        for (Map.Entry<String, String> me : map.tailMap("B、mldnjava").entrySet()) {
            System.out.println("\t|- " + me.getKey() + " --> " + me.getValue());
        }
        System.out.println("部分集合：");
        for (Map.Entry<String, String> me : map.subMap("A、mldn", "C、zhinangtuan").entrySet()) {
            System.out.println("\t|- " + me.getKey() + " --> " + me.getValue());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
