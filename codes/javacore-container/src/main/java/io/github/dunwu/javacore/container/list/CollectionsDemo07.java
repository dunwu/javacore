package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsDemo07 {

    public static void main(String[] args) {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "1、MLDN", "2、LXH", "3、mldnjava");
        System.out.println("交换之前的集合：" + all);
        Collections.swap(all, 0, 2);
        System.out.println("交换之后的集合：" + all);
    }

}
