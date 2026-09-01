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
public class SwitchDemo03 {

    /**
     * 演示 switch 的 default 分支：选项不在 case 列表中时走 default。
     */
    public static void demo() {
        int option = 4;
        switch (option) {
            case 1: {
                System.out.println("选择 1");
                break;
            }
            case 2: {
                System.out.println("选择 2");
                break;
            }
            case 3: {
                System.out.println("选择 3");
                break;
            }
            default: {
                System.out.println("无效选项");
                break;
            }
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 无效选项
