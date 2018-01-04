package io.github.dunwu.javase.array;

import java.util.Arrays;

/**
 * @author Zhang Peng
 */
public class ArraysDemo {

    private static void printDoubleArray(double[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();

    }

    public static void main(String[] args) {
        double[] array = new double[] { 7.6, 8.9, -5.7 };
        printDoubleArray(array);

        Arrays.sort(array);
        printDoubleArray(array);

        Arrays.fill(array, 5.6);
        printDoubleArray(array);
    }
}
