package io.github.dunwu.javacore.array;

import io.github.dunwu.javacore.array.util.ArrayUtil;
import java.util.Arrays;

public class ArrayRefDemo03 {
    public static void main(String[] args) {
        // 定义整型数组
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 };
        int[] age = { 31, 30, 18, 17, 8, 9, 1, 39 };
        // 数组排序并打印
        Arrays.sort(score);
        ArrayUtil.print(score);
        System.out.println("\n---------------------------");
        // 数组排序并打印
        Arrays.sort(age);
        ArrayUtil.print(age);
    }
};
