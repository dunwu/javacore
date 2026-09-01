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
public class SwitchDemo01 {

    /**
     * 演示 switch（char 类型）：根据运算符执行对应的四则运算。
     */
    public static void demo() {
        int x = 3;
        int y = 6;
        char oper = '+';
        switch (oper) {
            case '+': {
                System.out.println("x + y = " + (x + y));
                break;
            }
            case '-': {
                System.out.println("x - y = " + (x - y));
                break;
            }
            case '*': {
                System.out.println("x * y = " + (x * y));
                break;
            }
            case '/': {
                System.out.println("x / y = " + (x / y));
                break;
            }
            default: {
                System.out.println("未知的操作！");
                break;
            }
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// x + y = 9
