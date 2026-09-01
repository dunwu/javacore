package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 示例：Collections.addAll 向集合批量添加元素。
 */
public class CollectionsDemo02 {

    /** 演示 Collections.addAll 批量添加。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        Iterator<String> iter = all.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
