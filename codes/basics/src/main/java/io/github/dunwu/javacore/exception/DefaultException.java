package io.github.dunwu.javacore.exception;

/**
 * 自定义异常类，继承Exception类
 */
class MyException extends Exception {
    public MyException(String msg) {
        super(msg); // 调用Exception类中有一个参数的构造方法，传递错误信息
    }
};

public class DefaultException {
    public static void main(String[] args) {
        try {
            throw new MyException("自定义异常。"); // 抛出异常
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
