package io.github.dunwu.javacore.operator;

/**
 * 赋值操作符示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class AssignmentOperatorDemo {

    public static void main(String[] args) {
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
        System.out.println("c %= a = " + c);
        c <<= 2;
        System.out.println("c <<= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= 2 = " + c);
        c &= a;
        System.out.println("c &= a = " + c);
        c ^= a;
        System.out.println("c ^= a  = " + c);
        c |= a;
        System.out.println("c |= a  = " + c);
    }

}
// Output:
// c = a + b = 30
// c += a = 40
// c -= a = 30
// c *= a = 300
// c /= a = 1
// c %= a = 5
// c <<= 2 = 20
// c >>= 2 = 5
// c >>= 2 = 1
// c &= a = 0
// c ^= a = 10
// c |= a = 10
