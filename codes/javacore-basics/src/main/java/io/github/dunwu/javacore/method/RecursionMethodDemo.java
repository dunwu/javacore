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
// Output: 1\t1\t2\t3\t5\t8\t13\t21\t34\t
//
// 实际输出是用制表符分隔的一行，末尾还有一个制表符、且没有换行（demo() 用的是 print 而非 println）。
// 上面按字面写作 \t，否则真制表符会被 .editorconfig 的 trim_trailing_whitespace 规则删掉。
