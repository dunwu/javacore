package io.github.dunwu.javacore.util;

import java.util.Arrays;

/**
 * 示例：Arrays 工具类——排序、字符串输出、二分查找、填充。
 */
public class ArraysDemo {

    /**
     * 演示 Arrays 的常用操作。
     */
    public static void demo() {
        int[] temp = { 3, 4, 5, 7, 9, 1, 2, 6, 8 }; // 声明一个整型数组
        Arrays.sort(temp); // 进行排序的操作
        System.out.print("排序后的数组：");
        System.out.println(Arrays.toString(temp)); // 以字符串输出数组
        // 如果要想使用二分法查询的话，则必须是排序之后的数组
        int point = Arrays.binarySearch(temp, 3); // 检索位置
        System.out.println("元素‘3’的位置在：" + point);
        Arrays.fill(temp, 3); // 填充数组
        System.out.print("数组填充：");
        System.out.println(Arrays.toString(temp));
    }

    public static void main(String[] args) {
        demo();
    }

}
