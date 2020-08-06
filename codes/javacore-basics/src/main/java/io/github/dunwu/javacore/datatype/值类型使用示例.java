package io.github.dunwu.javacore.datatype;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class 值类型使用示例 {

    public static void demo1() {
        // compile error
        // int num = 9999999999999999999999999999999;
    }

    public static void demo10() {
        // 浮点型
        float f = 30.3f;
        // 强制类型转换
        int x = (int) f;
        System.out.println("x = " + x);
        // 执行强制转换
        System.out.println("10 / 3 = " + ((float) 10 / 3));
    }

    public static void demo2() {
        int max = Integer.MAX_VALUE;
        System.out.println("整型的最大值：" + max);
        System.out.println("整型的最大值 + 1：" + (max + 1));
        System.out.println("整型的最大值 + 2：" + (max + 2));
        System.out.println("整型的最大值 + 2：" + ((long) max + 2));
    }

    public static void demo3() {
        // 字符是使用”'“括起来的数据
        char ch1 = 'a';
        // 通过数字定义字符变量
        char ch2 = 97;
        System.out.println("ch1 = " + ch1);
        System.out.println("ch2 = " + ch2);
    }

    public static void demo4() {
        // 表示的是一个"
        char ch1 = '\"';
        // 表示的是一个、
        char ch2 = '\\';
        System.out.println("ch1 = " + ch1);
        System.out.println("ch2 = " + ch2);
        System.out.println("\"Hello World!\"");
    }

    public static void demo5() {
        // 定义一个浮点型变量
        float num = 3.0f;
        System.out.println("两个小数相乘：" + num * num);
    }

    public static void demo6() {
        // 定义布尔型变量
        boolean flag = true;
        // 打印输出
        System.out.println("flag = " + flag);
    }

    public static void demo7() {
        // 定义整型变量
        int x = 30;
        // 定义浮点型变量
        float y = 22.19f;
        System.out.println("x / y = " + (x / y));
        System.out.println("10 / 3.5 = " + (10 / 3.5));
        System.out.println("10 / 3 = " + (10 / 3));
    }

    public static void demo8() {
        // 定义字符串变量
        String str = "Zhang Peng";
        int x = 30;
        // 修改str的内容并将内容重新给str变量
        str = str + x;
        System.out.println("str = " + str);
    }

    public static void demo9() {
        // 定义整型变量
        int i = 1;
        // 定义整型变量
        int j = 2;
        System.out.println("1 + 2 = " + 1 + 2);
        System.out.println("1 + 2 = " + (1 + 2));
    }

    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (int i = 1; i <= 10; i++) {
            Method method = 值类型使用示例.class.getMethod("demo" + i);
            method.invoke(null);
        }
    }

}
