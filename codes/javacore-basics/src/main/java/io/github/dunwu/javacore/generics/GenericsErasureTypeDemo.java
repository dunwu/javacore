package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的类型擦除
 */
public class GenericsErasureTypeDemo {

    /**
     * 演示类型擦除：不同泛型参数的 List 在运行时是同一个 ArrayList 类。
     */
    public static void demo() {
        List<Object> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        System.out.println(list1.getClass());
        System.out.println(list2.getClass());
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// class java.util.ArrayList
// class java.util.ArrayList
