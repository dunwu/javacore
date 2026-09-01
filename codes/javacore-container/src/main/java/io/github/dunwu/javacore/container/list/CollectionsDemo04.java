package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 示例：Collections.binarySearch 二分查找。
 * <p>注意：binarySearch 要求集合已按自然序排序，否则结果不可预测（本例未排序，输出可能为负数）。
 */
public class CollectionsDemo04 {

    /** 演示 Collections.binarySearch 检索元素位置。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        int point = Collections.binarySearch(all, "LXH");    // 检索数据
        System.out.println("检索结果：" + point);
    }

    public static void main(String[] args) {
        demo();
    }

}
