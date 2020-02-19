package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayRefDemo {

    public static void main(String[] args) {
        int[] array = new int[] { 1, 3, 5 };
        fun(array);
    }

    private static void fun(int[] array) {
        for (int i : array) {
            System.out.print(i + "\t");
        }
    }

}
// Output:
// 1 3 5
