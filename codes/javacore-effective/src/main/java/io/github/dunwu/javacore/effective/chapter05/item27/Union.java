// Generic union method and program to exercise it - pages 129 - 130
package io.github.dunwu.javacore.effective.chapter05.item27;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Union {

    // Simple program to exercise generic method
    public static void main(String[] args) {
        Set<String> guys = new HashSet<String>(Arrays.asList("Tom", "Dick", "Harry"));
        Set<String> stooges = new HashSet<String>(Arrays.asList("Larry", "Moe", "Curly"));
        Set<String> aflCio = union(guys, stooges);
        System.out.println(aflCio);
    }

    // Generic method
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<E>(s1);
        result.addAll(s2);
        return result;
    }

}
