package io.github.dunwu.javacore.array;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ArrayRefDemo {

    /**
     * 演示数组作为方法参数：传递的是引用，方法内可直接读取数组内容。
     */
    public static void demo() {
        int[] array = new int[] { 1, 3, 5 };
        fun(array);
        System.out.println();
    }

    public static void main(String[] args) {
        demo();
    }

    private static void fun(int[] array) {
        for (int i : array) {
            System.out.print(i + "\t");
        }
    }

}
// Output:
// 1 3 5
