package io.github.dunwu.javacore.array;

import java.util.Arrays;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class MultiArrayDemo {

    /**
     * 演示多维数组：二维、三维与不规则（各行长度不同）数组的声明与打印。
     */
    public static void demo() {
        Integer[][] a1 = { // 自动装箱
            { 1, 2, 3, }, { 4, 5, 6, }, };
        Double[][][] a2 = { // 自动装箱
            { { 1.1, 2.2 }, { 3.3, 4.4 } }, { { 5.5, 6.6 }, { 7.7, 8.8 } }, { { 9.9, 1.2 }, { 2.3, 3.4 } }, };
        String[][] a3 = { { "The", "Quick", "Sly", "Fox" }, { "Jumped", "Over" },
            { "The", "Lazy", "Brown", "Dog", "and", "friend" }, };
        System.out.println("a1: " + Arrays.deepToString(a1));
        System.out.println("a2: " + Arrays.deepToString(a2));
        System.out.println("a3: " + Arrays.deepToString(a3));
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// a1: [[1, 2, 3], [4, 5, 6]]
// a2: [[[1.1, 2.2], [3.3, 4.4]], [[5.5, 6.6], [7.7, 8.8]], [[9.9, 1.2], [2.3, 3.4]]]
// a3: [[The, Quick, Sly, Fox], [Jumped, Over], [The, Lazy, Brown, Dog, and, friend]]
