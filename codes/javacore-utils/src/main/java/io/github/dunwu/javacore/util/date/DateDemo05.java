package io.github.dunwu.javacore.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 示例：SimpleDateFormat 先按模板 parse() 提取日期，再按新模板 format() 转换格式。
 */
public class DateDemo05 {

    /**
     * 演示字符串日期的提取与格式转换。
     */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
