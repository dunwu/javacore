package io.github.dunwu.javacore.container;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorDemo {

    public static void main(String[] args) {
        List<Character> list = new ArrayList<Character>();
        list.add('A');
        list.add('B');
        list.add('C');
        ListIterator<Character> listIterator = list.listIterator();
        System.out.println("由前向后输出：");
        while (listIterator.hasNext()) {
            Character c = listIterator.next();
            System.out.print(c + " ");
            listIterator.set(Character.toLowerCase(c));
        }
        System.out.println();

        System.out.println("由后向前输出：");
        while (listIterator.hasPrevious()) {
            System.out.print(listIterator.previous() + " ");
        }
        System.out.println();
    }

}
