package io.github.dunwu.javacore.operator;

/**
 * 算术操作符示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class MathOperatorDemo {

    public static void main(String[] args) {
        int x = 20;
        int y = 10;
        System.out.println("x + y = " + (x + y));
        System.out.println("x - y = " + (x - y));
        System.out.println("x * y = " + (x * y));
        System.out.println("x / y = " + (x / y));
        System.out.println("x % y = " + (x % y));
        System.out.println("x++ = " + x++);
        x = 20;
        System.out.println("++x = " + ++x);
    }

}
// output:
// x + y = 30
// x - y = 10
// x * y = 200
// x / y = 2
// x % y = 0
// x++ = 20
// ++x = 21
