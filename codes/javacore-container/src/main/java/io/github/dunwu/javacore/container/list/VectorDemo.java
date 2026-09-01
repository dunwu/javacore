package io.github.dunwu.javacore.container.list;

import java.util.List;
import java.util.Vector;

/**
 * 示例：Vector 是 JDK 1.0 就有的线程安全列表（每个方法都加锁），现已被 ArrayList 取代，仅作历史回顾。
 */
public class VectorDemo {

    /** 演示 Vector 的基本用法及独有的 addElement 方法。 */
    public static void demo() {
        List<String> list = new Vector<String>();
        list.add("B");
        list.add("C");
        list.add(0, "A");
        for (String s : list) {
            System.out.println(s);
        }

        Vector<String> vector = new Vector<String>();
        vector.addElement("X");
        vector.addElement("Y");
        vector.addElement("Z");
        for (String s : vector) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
