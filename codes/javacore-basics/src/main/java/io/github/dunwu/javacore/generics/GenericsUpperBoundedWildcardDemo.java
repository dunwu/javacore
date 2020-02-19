package io.github.dunwu.javacore.generics;

import java.util.Arrays;
import java.util.List;

/**
 * 类型边界之上限通配符
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see GenericsUpperBoundedWildcardDemo
 * @see GenericsLowerBoundedWildcardDemo
 * @see GenericsUnboundedWildcardDemo
 * @since 2019-03-21
 */
public class GenericsUpperBoundedWildcardDemo {

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3);
        System.out.println("sum = " + sumOfList(li));
    }

    public static double sumOfList(List<? extends Number> list) {
        double s = 0.0;
        for (Number n : list) {
            s += n.doubleValue();
        }
        return s;
    }

}
// Output:
// sum = 6.0
