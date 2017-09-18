package io.github.zp1024.javase.statement.condition;

public class IfDemo {
    public static void main(String args[]) {
        int x = 3; // 定义整型变量3
        int y = 10; // 定义整型变量10
        System.out.println("===== 比较开始 =====");
        if (x > y) {
            System.out.println("x比y大！");
        }
        if (x < y) {
            System.out.println("x比y小！");
        }
        System.out.println("===== 比较完成 =======");
    }
};
