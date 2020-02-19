package io.github.dunwu.javacore.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDemo05 {

    public static void main(String[] args) {
        String strDate = "2008-10-19 10:11:30.345";
        // 准备第一个模板，从字符串中提取出日期数字
        String pat1 = "yyyy-MM-dd HH:mm:ss.SSS";
        // 准备第二个模板，将提取后的日期数字变为指定的格式
        String pat2 = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1); // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2); // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(strDate); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        System.out.println(sdf2.format(d)); // 将日期变为新的格式
    }

}
