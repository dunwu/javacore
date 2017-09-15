package io.github.atlantis1024.javase.statement.condition;

public class IfElseDemo {
    public static void main(String args[]) {
        int x = 3; // 定义整型变量x
        if (x % 2 == 1) { // 判断于是是否为1
            System.out.println("x是奇数！"); // 如果余数为1表示奇数
        } else {
            System.out.println("x是偶数！"); // 如果余数为0表示是偶数
        }
    }
};
