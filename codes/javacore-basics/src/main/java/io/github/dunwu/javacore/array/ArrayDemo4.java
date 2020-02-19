package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayDemo4 {

    public static void main(String[] args) {
        int[] array = { 1, 2, 3 };
        for (int i = 0; i < array.length; i++) {
            array[i]++;
            System.out.println(String.format("array[%d] = %d", i, array[i]));
        }
    }

}
// Output:
// array[0] = 2
// array[1] = 3
// array[2] = 4
