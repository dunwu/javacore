package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的类型擦除
 */
public class GenericsErasureTypeDemo02 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        // List<Object> list2 = list; // Erorr
    }

}
// Output:
// class java.util.ArrayList
// class java.util.ArrayList
