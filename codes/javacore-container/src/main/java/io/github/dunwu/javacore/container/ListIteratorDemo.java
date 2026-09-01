package io.github.dunwu.javacore.container;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 示例：ListIterator 双向迭代器——可前向遍历、后向遍历，还可在遍历中修改元素。
 */
public class ListIteratorDemo {

    /** 演示 ListIterator 前向遍历时把元素改小写，再后向遍历输出。 */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
