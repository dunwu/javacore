package io.github.dunwu.javase.array;

/**
 * @author Zhang Peng
 */
public class ArrayHelper {
    /**
     * 在控制台打印数组元素
     * @param array
     */
    public static void print(int[] array) {
        for (int aTemp : array) {
            System.out.print(aTemp + "\t");
        }
        System.out.println();
    }
}
