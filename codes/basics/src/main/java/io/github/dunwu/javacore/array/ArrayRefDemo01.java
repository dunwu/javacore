package io.github.dunwu.javacore.array;

public class ArrayRefDemo01 {
    public static void main(String[] args) {
        // 利用静态初始化方式定义数组
        int[] array = { 1, 3, 5 };
        // 传递数组
        fun(array);
        for (int t : array) {
            System.out.print(t + "、");
        }
    }

    /**
     * 接收整型数组的引用
     * @param array
     */
    private static void fun(int[] array) {
        // 修改第一个元素
        array[0] = 6;
    }
};
