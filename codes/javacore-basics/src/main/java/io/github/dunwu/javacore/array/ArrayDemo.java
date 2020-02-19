package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@SuppressWarnings("all")
public class ArrayDemo {

    public static void main(String[] args) {
        int[] array1 = new int[2]; // 指定数组维度
        int[] array2 = new int[] { 1, 2 }; // 不指定数组维度

        System.out.println("array1 size is " + array1.length);
        for (int item : array1) {
            System.out.println(item);
        }

        System.out.println("array2 size is " + array1.length);
        for (int item : array2) {
            System.out.println(item);
        }
    }

}
// Output:
// array1 size is 2
// 0
// 0
// array2 size is 2
// 1
// 2
