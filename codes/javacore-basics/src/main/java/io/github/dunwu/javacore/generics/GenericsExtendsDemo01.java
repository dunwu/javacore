package io.github.dunwu.javacore.generics;

public class GenericsExtendsDemo01 {

    public static void main(String[] args) {
        System.out.println(max(3, 4, 5));
        System.out.println(max(6.6, 8.8, 7.7));
        System.out.println(max("pear", "apple", "orange"));
    }

    static <T extends Comparable<T>> T max(T x, T y, T z) {
        T max = x; // 假设x是初始最大值
        if (y.compareTo(max) > 0) {
            max = y; // y 更大
        }
        if (z.compareTo(max) > 0) {
            max = z; // 现在 z 更大
        }
        return max; // 返回最大对象
    }

}
// Output:
// 5
// 8.8
// pear
