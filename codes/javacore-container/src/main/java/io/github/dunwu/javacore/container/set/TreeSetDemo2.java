package io.github.dunwu.javacore.container.set;

import io.github.dunwu.javacore.container.bean.Person;

import java.util.Set;
import java.util.TreeSet;

/**
 * 示例：TreeSet 存放自定义对象 —— 依赖 Person 实现的 Comparable 排序；
 * compareTo 返回 0 的元素（三个王五）会被去重，只保留一份。
 */
public class TreeSetDemo2 {

    /** 演示 TreeSet 对自定义对象排序并去重。 */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
