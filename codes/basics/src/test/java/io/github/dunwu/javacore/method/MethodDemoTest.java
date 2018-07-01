package io.github.dunwu.javacore.method;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Zhang Peng
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MethodDemoTest {
    private void printInfo() {
        char[] chars = {'H', 'e', 'l', 'l', 'o', ',', 'L', 'X', 'H'}; // 定义字符数组
        for (char c : chars) { // 循环输出
            System.out.print(c);
        }
        System.out.println(""); // 换行
    }

    @Test
    public void demo01() {
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        printInfo(); // 调用printInfo()方法
        System.out.println("Hello World!!!");
    }

    // 定义方法，完成两个数字的相加操作，方法返回一个int型数据
    private int addOne(int x, int y) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法计算
        return temp; // 返回计算结果
    }

    // 定义方法，完成两个数字的相加操作，方法的返回值是一个float型数据
    private float addTwo(float x, float y) {
        float temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法操作
        return temp; // 返回计算结果
    }

    @Test
    public void demo02() {
        int one = addOne(10, 20); // 调用整型的加法操作
        float two = addTwo(10.3f, 13.3f); // 调用浮点数的加法操作
        System.out.println("addOne的计算结果：" + one);
        System.out.println("addTwo的计算结果：" + two);
    }

    // 定义方法，完成两个数字的相加操作，方法返回一个int型数据
    private int add(int x, int y) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法计算
        return temp; // 返回计算结果
    }

    private int add(int x, int y, int z) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y + z; // 执行加法计算
        return temp; // 返回计算结果
    }

    // 定义方法，完成两个数字的相加操作，方法的返回值是一个float型数据
    private float add(float x, float y) {
        float temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法操作
        return temp; // 返回计算结果
    }

    @Test
    public void demo03() {
        int one = add(10, 20); // 调用整型的加法操作
        float two = add(10.3f, 13.3f); // 调用浮点数的加法操作
        int three = add(10, 20, 30); // 调用有三个参数的加法操作
        System.out.println("add(int x,int y)的计算结果：" + one);
        System.out.println("add(float x,float y)的计算结果：" + two);
        System.out.println("(int x,int y,int z)的计算结果：" + three);
    }

    private void fun(int x) {
        System.out.println("3、进入fun()方法。");
        if (x == 10) {
            return; // 结束方法，返回被调用处
        }
        System.out.println("4、正常执行完fun()方法。");
    }

    @Test
    public void demo04() {
        System.out.println("1、调用fun()方法之前。");
        fun(10);
        fun(20);
        System.out.println("2、调用fun()方法之后。");
    }

    private int sum(int num) { // 定义方法用于求和操作
        if (num == 1) { // 判断是否是加到了最后一个数
            return 1;
        } else {
            return num + sum(num - 1); // 递归调用
        }
    }

    @Test
    public void demo05() {
        System.out.println("计算结果：" + sum(100)); // 调用操作
    }
}
