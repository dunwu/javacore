package io.github.dunwu.javase.statement;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Zhang Peng
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConditionStatementDemoTest {
    @Test
    public void testIf() {
        int x = 3; // 定义整型变量3
        int y = 10; // 定义整型变量10
        System.out.println("===== 比较开始 =====");
        if (x > y) {
            System.out.println("x比y大！");
        }
        if (x < y) {
            System.out.println("x比y小！");
        }
        System.out.println("===== 比较完成 =======");
    }

    @Test
    public void testIfAndElse() {
        int x = 3; // 定义整型变量x
        if (x % 2 == 1) { // 判断于是是否为1
            System.out.println("x是奇数！"); // 如果余数为1表示奇数
        } else {
            System.out.println("x是偶数！"); // 如果余数为0表示是偶数
        }
    }

    @Test
    public void testIfAndElseIfAndElse() {
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

    private void calc(int x, int y, char oper) {
        switch (oper) {
            case '+': { // 执行加法操作
                System.out.println("x + y = " + (x + y));
                break;
            }
            case '-': { // 执行减法操作
                System.out.println("x - y = " + (x - y));
                break;
            }
            case '*': { // 执行乘法操作
                System.out.println("x * y = " + (x * y));
                break;
            }
            case '/': { // 执行除法操作
                System.out.println("x / y = " + (x / y));
                break;
            }
            default: {
                System.out.println("未知的操作！");
                break;
            }
        }
    }

    @Test
    public void testSwitch() {
        calc(2, 10, '+');
        calc(2, 10, '*');
    }
}
