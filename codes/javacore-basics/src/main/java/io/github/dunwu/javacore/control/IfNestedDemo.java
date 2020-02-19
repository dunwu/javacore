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
public class IfNestedDemo {

    public static void main(String[] args) {
        int x = 30;
        int y = 10;

        if (x == 30) {
            if (y == 10) {
                System.out.print("X = 30 and Y = 10");
            }
        }
    }

}
// output:
// X = 30 and Y = 10
