package io.github.dunwu.javacore.operator;

/**
 * 逻辑操作符示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class LogicalOperatorDemo {

    /**
     * 演示逻辑操作符：&&（短路与）、||（短路或）、!（非）。
     */
    public static void demo() {
        boolean a = true;
        boolean b = false;

        System.out.println("a && b = " + (a && b));
        System.out.println("a || b = " + (a || b));
        System.out.println("!(a && b) = " + !(a && b));
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// a && b = false
// a || b = true
// !(a && b) = true
