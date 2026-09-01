package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-21
 */
public class GenericsWildcardDemo {

    /**
     * 演示通配符类型的协变：List<? extends Integer> 可以赋值给 List<? extends Number>。
     */
    public static void demo() {
        List<Integer> intList = new ArrayList<>();
        // List<Number> numList = intList; // Error

        List<? extends Integer> intList2 = new ArrayList<>();
        List<? extends Number> numList2 = intList2; // OK
    }

    public static void main(String[] args) {
        demo();
    }

}
