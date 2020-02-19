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
public class IfDemo {

    public static void main(String[] args) {
        int x = 3; // 定义整型变量3
        int y = 10; // 定义整型变量10
        if (x > y) {
            System.out.println("x比y大！");
        }
        if (x < y) {
            System.out.println("x比y小！");
        }
    }

}
// output;
// x比y小！
