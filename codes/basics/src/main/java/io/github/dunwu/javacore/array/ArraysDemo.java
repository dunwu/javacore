package io.github.dunwu.javacore.array;

import io.github.dunwu.javacore.array.util.ArrayUtil;
import java.util.Arrays;

/**
 * @author Zhang Peng
 */
public class ArraysDemo {
    public static void main(String[] args) {
        int[] array = new int[] { 4, 1, 5 };
        ArrayUtil.print(array);

        Arrays.sort(array);
        ArrayUtil.print(array);

        Arrays.fill(array, 6);
        ArrayUtil.print(array);
    }
}
