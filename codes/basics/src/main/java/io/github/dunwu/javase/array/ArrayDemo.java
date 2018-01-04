package io.github.dunwu.javase.array;

/**
 * @author Zhang Peng
 */
public class ArrayDemo {

    private static void createArray() {
        // 首选的风格
        int[] arr1;
        // 效果相同，但不是首选方法
        int arr2[];

        arr1 = new int[5];
        arr2 = new int[] { 1, 2 };

        // 数组变量的声明，和创建数组可以用一条语句完成
        int[] arr3 = new int[3];

        printIntArray(arr1);
        printIntArray(arr2);
        printIntArray(arr3);
    }

    private static void printIntArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println();

    }

    private static void printIntArray2(int[] array) {
        for (int item : array) {
            System.out.print(item + "\t");
        }
        System.out.println();

    }

    public static void main(String[] args) {
        createArray();
    }
}
