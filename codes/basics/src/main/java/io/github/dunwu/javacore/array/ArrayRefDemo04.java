package io.github.dunwu.javacore.array;

import io.github.dunwu.javacore.array.util.ArrayUtil;

public class ArrayRefDemo04 {
    public static void main(String[] args) {
        int[] src = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        int[] dest = { 11, 22, 33, 44, 55, 66, 77, 88, 99 };
        System.arraycopy(src, 0, dest, 0, 3);
        ArrayUtil.print(dest);
    }
};
