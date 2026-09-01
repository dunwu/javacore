package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 示例：Collections.swap 交换集合中两个指定位置的元素。
 */
public class CollectionsDemo07 {

    /** 演示 Collections.swap 交换元素。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "1、MLDN", "2、LXH", "3、mldnjava");
        System.out.println("交换之前的集合：" + all);
        Collections.swap(all, 0, 2);
        System.out.println("交换之后的集合：" + all);
    }

    public static void main(String[] args) {
        demo();
    }

}
