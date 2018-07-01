package io.github.dunwu.javacore.exception;

public class ThrowDemo01 {
    public static void main(String[] args) {
        try {
            throw new Exception("自己抛着玩的。"); // 抛出异常的实例化对象
        } catch (Exception e) {
            System.out.println(e);
        }
    }
};
