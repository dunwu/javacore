package io.github.dunwu.javacore.method;

/**
 * 演示方法的定义与调用：无参数、无返回值的方法。
 */
public class MethodDemo01 {

    public static void demo() {
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        System.out.println("Hello World!!!");
    }

    public static void main(String[] args) {
        demo();
    }

    private static void printInfo() {
        char[] array = { 'H', 'e', 'l', 'l', 'o', ',', 'L', 'X', 'H' };
        for (char a : array) {
            System.out.print(a);
        }
        System.out.println();
    }

}
