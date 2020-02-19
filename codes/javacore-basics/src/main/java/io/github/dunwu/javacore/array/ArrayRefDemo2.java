package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayRefDemo2 {

    public static void main(String[] args) {
        int[] array = fun();
        System.out.println(Arrays.toString(array));
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
