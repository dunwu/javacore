package io.github.dunwu.javase.array;

import java.util.Arrays;

/**
 * @author Zhang Peng
 */
public class ArraysDemo {
    public static void main(String[] args) {
        int[] array = new int[] { 4, 1, 5 };
        ArrayHelper.print(array);

        Arrays.sort(array);
        ArrayHelper.print(array);

        Arrays.fill(array, 6);
        ArrayHelper.print(array);
    }
}
