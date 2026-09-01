package io.github.dunwu.javacore.util.date;

import java.text.DateFormat;
import java.util.Date;

/**
 * 示例：DateFormat 工厂方法按默认地区格式化日期、日期时间。
 */
public class DateDemo03 {

    /**
     * 演示使用默认地区格式化日期。
     */
    public static void demo() {
        DateFormat df1 = null; // 声明一个DateFormat
        DateFormat df2 = null; // 声明一个DateFormat
        df1 = DateFormat.getDateInstance(); // 得到日期的DateFormat对象
        df2 = DateFormat.getDateTimeInstance(); // 得到日期时间的DateFormat对象
        System.out.println("DATE：" + df1.format(new Date())); // 按照日期格式化
        System.out.println("DATETIME：" + df2.format(new Date())); // 按照日期时间格式化
    }

    public static void main(String[] args) {
        demo();
    }

}
