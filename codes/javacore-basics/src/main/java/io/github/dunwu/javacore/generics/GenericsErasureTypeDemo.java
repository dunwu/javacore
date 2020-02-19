package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的类型擦除
 */
public class GenericsErasureTypeDemo {

    public static void main(String[] args) {
        List<Object> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        System.out.println(list1.getClass());
        System.out.println(list2.getClass());
    }

}
// Output:
// class java.util.ArrayList
// class java.util.ArrayList
