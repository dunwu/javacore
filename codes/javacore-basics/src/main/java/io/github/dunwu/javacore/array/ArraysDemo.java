package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArraysDemo {

    public static void main(String[] args) {
        int[] array = new int[] { 4, 1, 5 };
        System.out.println(Arrays.toString(array));

        Arrays.sort(array);
        System.out.println(Arrays.toString(array));

        Arrays.fill(array, 6);
        System.out.println(Arrays.toString(array));
    }

}
