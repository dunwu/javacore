package io.github.dunwu.javacore.control;

/**
 * 循环语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see WhileDemo
 * @see DoWhileDemo
 * @see ForDemo
 * @see ForNestedDemo
 * @see ForeachDemo
 */
public class ForeachDemo {

    /**
     * 演示增强 for（foreach）：依次遍历 int 数组与 String 数组，无需下标。
     */
    public static void demo() {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            System.out.print(x);
            System.out.print(",");
        }

        System.out.print("\n");
        String[] names = { "James", "Larry", "Tom", "Lacy" };

        for (String name : names) {
            System.out.print(name);
            System.out.print(",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 10,20,30,40,50,
// James,Larry,Tom,Lacy,
