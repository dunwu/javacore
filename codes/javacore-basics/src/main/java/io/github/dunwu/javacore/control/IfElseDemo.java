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
public class IfElseDemo {

    public static void main(String[] args) {
        int x = 3; // 定义整型变量x
        if (x % 2 == 1) { // 判断于是是否为1
            System.out.println("x是奇数！"); // 如果余数为1表示奇数
        } else {
            System.out.println("x是偶数！"); // 如果余数为0表示是偶数
        }
    }

}
// output:
// x是奇数！
