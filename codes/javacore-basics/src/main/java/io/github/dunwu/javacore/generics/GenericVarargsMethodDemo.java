package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 泛型方法与可变参数
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericVarargsMethodDemo {

    public static void main(String[] args) {
        List<String> ls = makeList("A");
        System.out.println(ls);
        ls = makeList("A", "B", "C");
        System.out.println(ls);
    }

    public static <T> List<T> makeList(T... args) {
        List<T> result = new ArrayList<T>();
        Collections.addAll(result, args);
        return result;
    }

}
// Output:
// [A]
// [A, B, C]
