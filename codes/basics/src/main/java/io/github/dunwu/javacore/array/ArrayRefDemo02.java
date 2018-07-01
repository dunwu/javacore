package io.github.dunwu.javacore.array;

import io.github.dunwu.javacore.array.util.ArrayUtil;

public class ArrayRefDemo02 {
    public static void main(String[] args) {
        // 通过方法实例化数组
        int[] array = fun();
        // 打印数组内容
        ArrayUtil.print(array);

    }

    /**
     * 返回一个数组
     * @return
     */
    private static int[] fun() {
        return new int[] { 1, 3, 5, 7, 9 };
    }
};
