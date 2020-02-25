package io.github.dunwu.javacore.container.set;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.Set;
import java.util.TreeSet;

public class TreeSetDemo2 {

    public static void main(String[] args) {
        Set<Person> treeSet = new TreeSet<Person>();
        treeSet.add(new Person("张三", 30));
        treeSet.add(new Person("李四", 31));
        treeSet.add(new Person("王五", 32));
        treeSet.add(new Person("王五", 32));
        treeSet.add(new Person("王五", 32));
        treeSet.add(new Person("赵六", 33));
        treeSet.add(new Person("孙七", 33));
        System.out.println(treeSet);
    }

}
