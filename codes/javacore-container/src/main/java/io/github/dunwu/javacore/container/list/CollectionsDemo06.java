package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 示例：Collections.sort 按自然序对集合排序。
 */
public class CollectionsDemo06 {

    /** 演示 Collections.sort 排序。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "1、MLDN", "2、LXH", "3、mldnjava");
        Collections.addAll(all, "B、www.mldn.cn");
        Collections.addAll(all, "A、www.mldnjava.cn");
        System.out.println("排序之前的集合：" + all);
        Collections.sort(all);
        System.out.println("排序之后的集合：" + all);
    }

    public static void main(String[] args) {
        demo();
    }

}
