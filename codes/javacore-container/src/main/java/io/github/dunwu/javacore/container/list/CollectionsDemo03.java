package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 示例：Collections.reverse 反转集合元素顺序。
 */
public class CollectionsDemo03 {

    /** 演示 Collections.reverse 内容反转。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        Collections.reverse(all);    // 内容反转
        Iterator<String> iter = all.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
