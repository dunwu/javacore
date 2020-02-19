// Concrete implementation built atop skeletal implementation - Page 95
package io.github.dunwu.javacore.effective.chapter04.item18;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class IntArrays {

    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        List<Integer> list = intArrayAsList(a);

        Collections.shuffle(list);
        System.out.println(list);
    }

    static List<Integer> intArrayAsList(final int[] a) {
        if (a == null) { throw new NullPointerException(); }

        return new AbstractList<Integer>() {
            @Override
            public Integer get(int i) {
                return a[i]; // Autoboxing (Item 5)
            }

            @Override
            public Integer set(int i, Integer val) {
                int oldVal = a[i];
                a[i] = val; // Auto-unboxing
                return oldVal; // Autoboxing
            }

            @Override
            public int size() {
                return a.length;
            }
        };
    }

}
