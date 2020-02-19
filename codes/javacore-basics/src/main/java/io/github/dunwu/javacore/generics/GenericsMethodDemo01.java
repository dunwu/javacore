package io.github.dunwu.javacore.generics;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsMethodDemo01 {

    public static void main(String[] args) {
        printClass("abc");
        printClass(10);
    }

    public static <T> void printClass(T obj) {
        System.out.println(obj.getClass().toString());
    }

}
// Output:
// class java.lang.String
// class java.lang.Integer
