package io.github.dunwu.javacore.generics;

/**
 * 演示泛型上界：类型参数限定为 Comparable 的子类型。
 */
public class GenericsExtendsDemo01 {

    /**
     * 分别求整数、小数、字符串的最大值。
     */
    public static void demo() {
        System.out.println(max(3, 4, 5));
        System.out.println(max(6.6, 8.8, 7.7));
        System.out.println(max("pear", "apple", "orange"));
    }

    public static void main(String[] args) {
        demo();
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
