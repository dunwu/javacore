package io.github.dunwu.javacore.exception;

public class ExceptionDemo04 {
    public static void main(String[] args) {
        System.out.println("********** 计算开始 ***********");
        int i = 0; // 定义整型变量
        int j = 0; // 定义整型变量
        try {
            String str1 = args[0]; // 接收第一个参数
            String str2 = args[1]; // 接收第二个参数
            i = Integer.parseInt(str1); // 将第一个参数由字符串变为整型
            j = Integer.parseInt(str2); // 将第二个参数由字符串变为整型
            int temp = i / j; // 此处产生了异常
            System.out.println("两个数字相除的结果：" + temp);
            System.out.println("----------------------------");
        } catch (ArithmeticException e) { // 捕获算术异常
            System.out.println("出现异常了：" + e);
        }
        System.out.println("********** 计算结束 ***********");
    }
};
