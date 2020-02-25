package io.github.dunwu.javacore.container.list;

import java.util.List;
import java.util.Vector;

public class VectorDemo {

    public static void main(String[] args) {
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

}
