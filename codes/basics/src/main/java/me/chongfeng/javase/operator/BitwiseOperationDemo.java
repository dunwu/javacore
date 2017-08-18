package me.chongfeng.javase.operator;

/**
 * 位运算符示例
 */
public class BitwiseOperationDemo {
    public static void main(String args[]) {
        int a = 60; /* 60 = 0011 1100 */
        int b = 13; /* 13 = 0000 1101 */
        int c = 0;
        c = a & b; /* 12 = 0000 1100 */
        System.out.println("a & b = " + c);

        c = a | b; /* 61 = 0011 1101 */
        System.out.println("a | b = " + c);

        c = a ^ b; /* 49 = 0011 0001 */
        System.out.println("a ^ b = " + c);

        c = ~a; /*-61 = 1100 0011 */
        System.out.println("~a = " + c);

        c = a << 2; /* 240 = 1111 0000 */
        System.out.println("a << 2 = " + c);

        c = a >> 2; /* 15 = 1111 */
        System.out.println("a >> 2  = " + c);

        c = a >>> 2; /* 15 = 0000 1111 */
        System.out.println("a >>> 2 = " + c);
    }
};
