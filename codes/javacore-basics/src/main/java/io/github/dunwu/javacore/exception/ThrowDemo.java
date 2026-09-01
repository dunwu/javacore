package io.github.dunwu.javacore.exception;

/**
 * 演示 throw：在方法内部主动抛出一个异常对象。
 */
public class ThrowDemo {

    public static void demo() {
        f();
    }

    public static void main(String[] args) {
        demo();
    }

    public static void f() {
        try {
            throw new RuntimeException("抛出一个异常");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
