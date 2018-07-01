package io.github.dunwu.javacore.control;

/**
 * 分支语句示例
 * @see IfDemo
 * @see IfElseDemo
 * @see IfElseifElseDemo
 * @see IfNestedDemo
 * @see SwitchDemo01
 * @see SwitchDemo02
 * @see SwitchDemo03
 * @author Zhang Peng
 */
public class SwitchDemo03 {
    public static void main(String[] args) {
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
};
// output:
// 无效选项
