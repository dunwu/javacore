// Broken comparator - can you spot the flaw? - Page 221
package io.github.dunwu.javacore.effective.chapter07.item49;

import java.util.Comparator;

public class BrokenComparator {

    public static void main(String[] args) {

        // Broken comparator - can you spot the flaw? - Page 221
        Comparator<Integer> naturalOrder = new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                return first < second ? -1 : (first.equals(second) ? 0 : 1);
            }
        };

        // Fixed Comparator - Page 222
        // Comparator<Integer> naturalOrder = new Comparator<Integer>() {
        // public int compare(Integer first, Integer second) {
        // int f = first; // Auto-unboxing
        // int s = second; // Auto-unboxing
        // return f < s ? -1 : (f == s ? 0 : 1); // No unboxing
        // }
        // };

        int result = naturalOrder.compare(new Integer(42), new Integer(42));
        System.out.println(result);
    }

}
