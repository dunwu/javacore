package io.github.dunwu.javacore.exception;

public class ThrowDemo {

    public static void main(String[] args) {
        f();
    }

    public static void f() {
        try {
            throw new RuntimeException("抛出一个异常");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
