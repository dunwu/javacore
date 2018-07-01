package io.github.dunwu.javacore.array;

/**
 * @author Zhang Peng
 */
public class ArrayDemo {
    public static void main(String[] args) {
        // 首选的风格
        int[] arr1;
        // 效果相同，但不是首选方法
        int arr2[];

        arr1 = new int[5];
        arr2 = new int[] { 1, 2 };

        // 数组变量的声明，和创建数组可以用一条语句完成
        int[] arr3 = new int[3];

        System.out.println("arr1: ");
        for (int item : arr1) {
            System.out.println(item);
        }

        System.out.println("arr2: ");
        for (int item : arr2) {
            System.out.println(item);
        }

        System.out.println("arr3: ");
        for (int item : arr3) {
            System.out.println(item);
        }
    }
}
