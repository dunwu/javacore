package io.github.dunwu.javacore.util.date;

import java.text.DateFormat;
import java.util.Date;

public class DateDemo03 {

    public static void main(String[] args) {
        DateFormat df1 = null; // 声明一个DateFormat
        DateFormat df2 = null; // 声明一个DateFormat
        df1 = DateFormat.getDateInstance(); // 得到日期的DateFormat对象
        df2 = DateFormat.getDateTimeInstance(); // 得到日期时间的DateFormat对象
        System.out.println("DATE：" + df1.format(new Date())); // 按照日期格式化
        System.out.println("DATETIME：" + df2.format(new Date())); // 按照日期时间格式化
    }

}
