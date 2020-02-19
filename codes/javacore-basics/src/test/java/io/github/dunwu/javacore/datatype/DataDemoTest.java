package io.github.dunwu.javacore.datatype;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataDemoTest {

    @Test
    public void demo01() {
        // compile error
        // int num = 9999999999999999999999999999999;
    }

    @Test
    public void demo02() {
        int max = Integer.MAX_VALUE;
        System.out.println("整型的最大值：" + max);
        System.out.println("整型的最大值 + 1：" + (max + 1));
        System.out.println("整型的最大值 + 2：" + (max + 2));
        System.out.println("整型的最大值 + 2：" + ((long) max + 2));
    }

    @Test
    public void demo03() {
        // 字符是使用”'“括起来的数据
        char ch1 = 'a';
        // 通过数字定义字符变量
        char ch2 = 97;
        System.out.println("ch1 = " + ch1);
        System.out.println("ch2 = " + ch2);
    }

    @Test
    public void demo04() {
        // 表示的是一个"
        char ch1 = '\"';
        // 表示的是一个、
        char ch2 = '\\';
        System.out.println("ch1 = " + ch1);
        System.out.println("ch2 = " + ch2);
        System.out.println("\"Hello World!\"");
    }

    @Test
    public void demo05() {
        // 定义一个浮点型变量
        float num = 3.0f;
        System.out.println("两个小数相乘：" + num * num);
    }

    @Test
    public void demo06() {
        // 定义布尔型变量
        boolean flag = true;
        // 打印输出
        System.out.println("flag = " + flag);
    }

    @Test
    public void demo07() {
        // 定义整型变量
        int x = 30;
        // 定义浮点型变量
        float y = 22.19f;
        System.out.println("x / y = " + (x / y));
        System.out.println("10 / 3.5 = " + (10 / 3.5));
        System.out.println("10 / 3 = " + (10 / 3));
    }

    @Test
    public void demo08() {
        // 定义字符串变量
        String str = "Zhang Peng";
        int x = 30;
        // 修改str的内容并将内容重新给str变量
        str = str + x;
        System.out.println("str = " + str);
    }

    @Test
    public void demo09() {
        // 定义整型变量
        int i = 1;
        // 定义整型变量
        int j = 2;
        System.out.println("1 + 2 = " + 1 + 2);
        System.out.println("1 + 2 = " + (1 + 2));
    }

    @Test
    public void demo10() {
        // 浮点型
        float f = 30.3f;
        // 强制类型转换
        int x = (int) f;
        System.out.println("x = " + x);
        // 执行强制转换
        System.out.println("10 / 3 = " + ((float) 10 / 3));
    }

}
