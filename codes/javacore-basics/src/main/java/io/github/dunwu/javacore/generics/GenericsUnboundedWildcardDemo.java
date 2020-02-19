package io.github.dunwu.javacore.generics;

import java.util.Arrays;
import java.util.List;

/**
 * 类型边界之无边界通配符
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see GenericsUpperBoundedWildcardDemo
 * @see GenericsLowerBoundedWildcardDemo
 * @see GenericsUnboundedWildcardDemo
 * @since 2019-03-21
 */
public class GenericsUnboundedWildcardDemo {

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3);
        List<String> ls = Arrays.asList("one", "two", "three");
        printList(li);
        printList(ls);
    }

    public static void printList(List<?> list) {
        for (Object elem : list) {
            System.out.print(elem + " ");
        }
        System.out.println();
    }

}
// Output:
// 1 2 3
// one two three
