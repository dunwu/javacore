package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * 示例：{@link java.util.Arrays} 工具类常用方法 —— toString、sort、fill。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArraysDemo {

    /**
     * 演示 Arrays 的打印、排序、填充操作。
     */
    public static void demo() {
        int[] array = new int[] { 4, 1, 5 };
        System.out.println(Arrays.toString(array));

        Arrays.sort(array);
        System.out.println(Arrays.toString(array));

        Arrays.fill(array, 6);
        System.out.println(Arrays.toString(array));
    }

    public static void main(String[] args) {
        demo();
    }

}
