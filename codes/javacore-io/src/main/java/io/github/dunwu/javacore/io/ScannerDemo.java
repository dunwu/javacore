package io.github.dunwu.javacore.io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ScannerDemo {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // 从键盘接收数据
        int i = 0;
        float f = 0.0f;
        System.out.print("输入整数：");
        if (scan.hasNextInt()) { // 判断输入的是否是整数
            i = scan.nextInt(); // 接收整数
            System.out.println("整数数据：" + i);
        } else {
            System.out.println("输入的不是整数！");
        }

        System.out.print("输入小数：");
        if (scan.hasNextFloat()) { // 判断输入的是否是小数
            f = scan.nextFloat(); // 接收小数
            System.out.println("小数数据：" + f);
        } else {
            System.out.println("输入的不是小数！");
        }

        Date date = null;
        String str = null;
        System.out.print("输入日期（yyyy-MM-dd）：");
        if (scan.hasNext("^\\d{4}-\\d{2}-\\d{2}$")) { // 判断
            str = scan.next("^\\d{4}-\\d{2}-\\d{2}$"); // 接收
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            } catch (Exception e) {
            }
        } else {
            System.out.println("输入的日期格式错误！");
        }
        System.out.println(date);
    }

}
