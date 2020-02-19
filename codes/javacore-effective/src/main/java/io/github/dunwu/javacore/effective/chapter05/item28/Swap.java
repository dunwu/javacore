// Private helper method for wildcard capture - Pages 139-140
package io.github.dunwu.javacore.effective.chapter05.item28;

import java.util.Arrays;
import java.util.List;

public class Swap {

    public static void main(String[] args) {
        // Swap the first and last argument and print the resulting list
        List<String> argList = Arrays.asList(args);
        swap(argList, 0, argList.size() - 1);
        System.out.println(argList);
    }

    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    // Private helper method for wildcard capture
    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

}
