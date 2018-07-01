package io.github.dunwu.javacore.exception;

public class ExceptionDemo02 {
    public static void main(String[] args) {
        System.out.println("********** 计算开始 ***********");
        int i = 10; // 定义整型变量
        int j = 0; // 定义整型变量
        try {
            int temp = i / j; // 此处产生了异常
            System.out.println("两个数字相除的结果：" + temp);
            System.out.println("----------------------------");
        } catch (ArithmeticException e) {
            System.out.println("出现异常了：" + e);
        }
        System.out.println("********** 计算结束 ***********");
    }
};
