package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类型边界之下限通配符
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see GenericsUpperBoundedWildcardDemo
 * @see GenericsLowerBoundedWildcardDemo
 * @see GenericsUnboundedWildcardDemo
 * @since 2019-03-21
 */
public class GenericsLowerBoundedWildcardDemo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        addNumbers(list);
        System.out.println(Arrays.deepToString(list.toArray()));
    }

    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }

}
// Output:
// [1, 2, 3, 4, 5]
