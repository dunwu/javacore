package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayDemo4 {

    /**
     * 演示遍历数组并修改元素：每个元素自增 1。
     */
    public static void demo() {
        int[] array = { 1, 2, 3 };
        for (int i = 0; i < array.length; i++) {
            array[i]++;
            System.out.println(String.format("array[%d] = %d", i, array[i]));
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// array[0] = 2
// array[1] = 3
// array[2] = 4
