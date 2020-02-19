package io.github.dunwu.javacore.generics;

/**
 * 泛型数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericArrayDemo {

    public static void main(String[] args) {
        // 放开注释会报错，泛型不支持值类型
        // int[] iArray = {1, 2, 3};
        // print(iArray);

        Integer[] iArray = { 1, 2, 3, 4, 5 };
        print(iArray);

        Character[] cArray = { 'H', 'E', 'L', 'L', 'O' };
        print(cArray);
    }

    public static <T> void print(T[] array) {
        for (T item : array) {
            System.out.printf(item + "\t");
        }
        System.out.println();
    }

}
// Output:
// 1 2 3 4 5
// H E L L O
