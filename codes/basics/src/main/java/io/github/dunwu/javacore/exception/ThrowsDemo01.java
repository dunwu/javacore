package io.github.dunwu.javacore.exception;

public class ThrowsDemo01 {
    public static void main(String[] args) {
        Math m = new Math(); // 实例化Math类对象
        try {
            System.out.println("除法操作：" + m.div(10, 2));
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常
        }
    }
};
