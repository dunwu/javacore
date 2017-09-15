package io.github.atlantis1024.javase.operator;

/**
 * 逻辑运算符示例
 */
public class LogicalOperatorDemo {
    public static void main(String args[]) {
        boolean a = true;
        boolean b = false;
        System.out.println("a && b = " + (a && b));
        System.out.println("a || b = " + (a || b));
        System.out.println("!(a && b) = " + !(a && b));
    }
};
