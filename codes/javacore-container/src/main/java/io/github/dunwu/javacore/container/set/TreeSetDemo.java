package io.github.dunwu.javacore.container.set;

import java.util.SortedSet;
import java.util.TreeSet;

public class TreeSetDemo {

    public static void main(String[] args) {
        SortedSet<String> treeSet = new TreeSet<String>();
        treeSet.add("A");
        treeSet.add("B");
        treeSet.add("C");
        treeSet.add("C");
        treeSet.add("C");
        treeSet.add("D");
        treeSet.add("E");
        System.out.println("第一个元素：" + treeSet.first());
        System.out.println("最后一个元素：" + treeSet.last());
        System.out.println("headSet元素：" + treeSet.headSet("C"));
        System.out.println("tailSet元素：" + treeSet.tailSet("C"));
        System.out.println("subSet元素：" + treeSet.subSet("B", "D"));
    }

}
