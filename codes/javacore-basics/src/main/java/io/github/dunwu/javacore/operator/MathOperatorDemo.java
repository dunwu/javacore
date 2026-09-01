package io.github.dunwu.javacore.operator;

/**
 * 算术操作符示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class MathOperatorDemo {

    /**
     * 演示算术操作符：+、-、*、/、% 以及自增的两种形式（x++ 先用后加，++x 先加后用）。
     */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
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
