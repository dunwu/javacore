package io.github.dunwu.javacore.exception;

public class ThrowDemo02 {
    public static void main(String[] args) {
        Math m = new Math();
        try {
            System.out.println("除法操作：" + m.div(10, 0));
        } catch (Exception e) {
            System.out.println("异常产生：" + e);
        }
    }
};
