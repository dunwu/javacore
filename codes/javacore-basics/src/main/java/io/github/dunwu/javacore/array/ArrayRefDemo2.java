package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayRefDemo2 {

    /**
     * 演示数组作为方法返回值：方法可以直接返回一个数组引用。
     */
    public static void demo() {
        int[] array = fun();
        System.out.println(Arrays.toString(array));
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 返回一个数组
     */
    private static int[] fun() {
        return new int[] { 1, 3, 5 };
    }

}
// Output:
// [1, 3, 5]
