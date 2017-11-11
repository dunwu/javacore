package io.github.dunwu.javase.operator;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Zhang Peng
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OperatorDemoTest {
    /**
     * 算术操作符
     */
    @Test
    public void testArithmetic() {
        int a = 10;
        int b = 20;
        int c = 25;
        int d = 25;
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("b / a = " + (b / a));
        System.out.println("b % a = " + (b % a));
        System.out.println("c % a = " + (c % a));
        System.out.println("a++   = " + (a++));
        System.out.println("++a   = " + (++a));
        System.out.println("a--   = " + (a--));
        System.out.println("--a   = " + (--a));
        System.out.println("d++   = " + (d++));
        System.out.println("++d   = " + (++d));
    }

    /**
     * 赋值操作符
     */
    @Test
    public void testAssigning() {
        int a = 10;
        int b = 20;
        int c = 0;
        c = a + b;
        System.out.println("c = a + b = " + c);
        c += a;
        System.out.println("c += a  = " + c);
        c -= a;
        System.out.println("c -= a = " + c);
        c *= a;
        System.out.println("c *= a = " + c);
        a = 10;
        c = 15;
        c /= a;
        System.out.println("c /= a = " + c);
        a = 10;
        c = 15;
        c %= a;
        System.out.println("c %= a  = " + c);
        c <<= 2;
        System.out.println("c <<= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= a = " + c);
        c &= a;
        System.out.println("c &= 2  = " + c);
        c ^= a;
        System.out.println("c ^= a   = " + c);
        c |= a;
        System.out.println("c |= a   = " + c);
    }

    /**
     * 位运算操作符
     */
    @Test
    public void testBitwise() {
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

    /**
     * 条件运算操作符
     */
    @Test
    public void testConditional() {
        int max = 0; // 保存最大值
        int x = 3; // 定义整型变量x
        int y = 10;
        max = x > y ? x : y; // 通过三目运算符求出最大值
        System.out.println("最大值为：" + max);
    }

    /**
     * 逻辑运算操作符
     */
    @Test
    public void testLogical() {
        boolean a = true;
        boolean b = false;
        System.out.println("a && b = " + (a && b));
        System.out.println("a || b = " + (a || b));
        System.out.println("!(a && b) = " + !(a && b));
    }

    /**
     * 关系运算操作符
     */
    @Test
    public void testRelational() {
        int a = 10;
        int b = 20;
        System.out.println("a == b = " + (a == b));
        System.out.println("a != b = " + (a != b));
        System.out.println("a > b = " + (a > b));
        System.out.println("a < b = " + (a < b));
        System.out.println("b >= a = " + (b >= a));
        System.out.println("b <= a = " + (b <= a));
    }

    class Animal {}

    class Cat extends Animal {}

    /**
     * instanceof 操作符
     */
    @Test
    public void testInstanceof() {
        Animal animal = new Cat();
        boolean result = animal instanceof Cat;
        System.out.println(result);
    }

    @Test
    public void testTypeChange() {
        char ch = 'a';
        short a = -2;
        int b = 3;
        float f = 5.3f;
        double d = 6.28;
        System.out.print("(ch / a) - (d / f) - (a + b) = ");
        System.out.println((ch / a) - (d / f) - (a + b));
    }
}
