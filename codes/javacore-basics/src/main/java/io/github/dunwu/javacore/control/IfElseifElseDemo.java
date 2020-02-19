package io.github.dunwu.javacore.control;

/**
 * 分支语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see IfDemo
 * @see IfElseDemo
 * @see IfElseifElseDemo
 * @see IfNestedDemo
 * @see SwitchDemo01
 * @see SwitchDemo02
 * @see SwitchDemo03
 */
public class IfElseifElseDemo {

    public static void main(String[] args) {
        int x = 5; // 定义整型变量x
        if (x == 1) {
            System.out.println("x的值是1！");
        } else if (x == 2) {
            System.out.println("x的值是2！");
        } else if (x == 3) {
            System.out.println("x的值是3！");
        } else {
            System.out.println("x的值不是1、2、3中的一个！");
        }
    }

}
// output:
// x的值不是1、2、3中的一个！
