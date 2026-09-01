package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的类型擦除
 */
public class GenericsErasureTypeDemo02 {

    /**
     * 演示类型擦除的编译期约束：List<Integer> 不能赋值给 List<Object>（放开注释即编译报错）。
     */
    public static void demo() {
        List<Integer> list = new ArrayList<>();
        // List<Object> list2 = list; // Erorr
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// class java.util.ArrayList
// class java.util.ArrayList
