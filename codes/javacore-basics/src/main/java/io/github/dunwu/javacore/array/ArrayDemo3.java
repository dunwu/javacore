package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@SuppressWarnings("all")
public class ArrayDemo3 {

    public static void main(String[] args) {
        int length = 3;
        // 放开被注掉的代码，编译器会报错
        // int[] array = new int[4.0];
        // int[] array2 = new int["test"];
        int[] array3 = new int['a'];
        int[] array4 = new int[length];
        int[] array5 = new int[length + 2];
        int[] array6 = new int['a' + 2];
        // int[] array7 = new int[length + 2.1];
        System.out.println("array3.length = [" + array3.length + "]");
        System.out.println("array4.length = [" + array4.length + "]");
        System.out.println("array5.length = [" + array5.length + "]");
        System.out.println("array6.length = [" + array6.length + "]");
    }

}
// Output:
// array3.length = [97]
// array4.length = [3]
// array5.length = [5]
// array6.length = [99]
