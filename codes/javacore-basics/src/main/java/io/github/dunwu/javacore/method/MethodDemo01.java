package io.github.dunwu.javacore.method;

public class MethodDemo01 {

    public static void main(String[] args) {
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        System.out.println("Hello World!!!");
    }

    private static void printInfo() {
        char[] array = { 'H', 'e', 'l', 'l', 'o', ',', 'L', 'X', 'H' };
        for (char a : array) {
            System.out.print(a);
        }
        System.out.println();
    }

}
