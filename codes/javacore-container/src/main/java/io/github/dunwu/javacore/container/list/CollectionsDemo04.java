package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsDemo04 {

    public static void main(String[] args) {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        int point = Collections.binarySearch(all, "LXH");    // 检索数据
        System.out.println("检索结果：" + point);
    }

}
