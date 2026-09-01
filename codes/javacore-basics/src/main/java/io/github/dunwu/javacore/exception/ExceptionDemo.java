package io.github.dunwu.javacore.exception;

/**
 * 演示受检异常：调用抛出受检异常的方法时，必须捕获或继续声明，否则编译不通过。
 */
public class ExceptionDemo {

    public static void main(String[] args) {
        // 放开下面的注释会报错。因为 getMethod 抛出 Exception，编译器会检查 Exception。如果没有 try catch 会报错。
        // Method sum = String.class.getMethod("toString", int.class);
    }

}
