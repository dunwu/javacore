package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class RecursionMethodDemo {

    public static int fib(int num) {
        if (num == 1 || num == 2) {
            return 1;
        } else {
            return fib(num - 2) + fib(num - 1);
        }
    }

    /**
     * 演示递归：输出斐波那契数列前 9 项。
     */
    public static void demo() {
        for (int i = 1; i < 10; i++) {
            System.out.print(fib(i) + "\t");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
