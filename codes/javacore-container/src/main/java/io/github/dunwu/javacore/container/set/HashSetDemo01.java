package io.github.dunwu.javacore.container.set;

import java.util.HashSet;
import java.util.Set;

public class HashSetDemo01 {

    public static void main(String[] args) {
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

}
