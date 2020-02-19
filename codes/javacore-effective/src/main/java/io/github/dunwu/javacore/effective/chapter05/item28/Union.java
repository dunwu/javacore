// Generic union method with wildcard types - Pages 137-138
package io.github.dunwu.javacore.effective.chapter05.item28;

import java.util.HashSet;
import java.util.Set;

public class Union {

    // Simple program to exercise flexible generic method
    public static void main(String[] args) {
        Set<Integer> integers = new HashSet<Integer>();
        integers.add(1);
        integers.add(3);
        integers.add(5);

        Set<Double> doubles = new HashSet<Double>();
        doubles.add(2.0);
        doubles.add(4.0);
        doubles.add(6.0);

        // Won't compile; see page 137
        // Set<Number> numbers = union(integers, doubles);

        // Explicit type parameter is necessary here
        Set<Number> numbers = Union.<Number>union(integers, doubles);

        System.out.println(numbers);
    }

    public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
        Set<E> result = new HashSet<E>(s1);
        result.addAll(s2);
        return result;
    }

}
