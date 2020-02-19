package io.github.dunwu.javacore.util.math;

import java.text.NumberFormat;

public class NumberFormatDemo01 {

    public static void main(String[] args) {
        NumberFormat nf = null; // 声明一个NumberFormat对象
        nf = NumberFormat.getInstance(); // 得到默认的数字格式化显示
        System.out.println("格式化之后的数字：" + nf.format(10000000));
        System.out.println("格式化之后的数字：" + nf.format(1000.345));
    }

}
