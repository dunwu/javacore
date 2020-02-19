package io.github.dunwu.javacore.operator;

/**
 * 逻辑操作符示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class LogicalOperatorDemo {

    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;

        System.out.println("a && b = " + (a && b));
        System.out.println("a || b = " + (a || b));
        System.out.println("!(a && b) = " + !(a && b));
    }

}
// output:
// a && b = false
// a || b = true
// !(a && b) = true
