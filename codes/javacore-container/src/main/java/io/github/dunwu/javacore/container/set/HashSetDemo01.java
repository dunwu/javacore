package io.github.dunwu.javacore.container.set;

import java.util.HashSet;
import java.util.Set;

/**
 * 示例：HashSet 不允许重复元素 —— 重复 add 只保留一份；不保证遍历顺序。
 */
public class HashSetDemo01 {

    /** 演示 HashSet 去重。 */
    public static void demo() {
        Set<String> allSet = new HashSet<String>();
        allSet.add("A");    // 增加内容
        allSet.add("B");    // 增加内容
        allSet.add("C");    // 增加内容
        allSet.add("C");    // 重复内容
        allSet.add("C");    // 重复内容
        allSet.add("D");    // 增加内容
        allSet.add("E");    // 增加内容
        System.out.println(allSet);
    }

    public static void main(String[] args) {
        demo();
    }

}
