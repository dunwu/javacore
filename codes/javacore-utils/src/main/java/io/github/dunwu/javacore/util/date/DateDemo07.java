package io.github.dunwu.javacore.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

class DateTime2 {

    // 以后直接通过此类就可以取得日期时间
    private SimpleDateFormat sdf = null; // 声明SimpleDateFormat对象

    public String getDate() { // 得到的是一个日期：格式为：yyyy-MM-dd HH:mm:ss.SSS
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return this.sdf.format(new Date());// 将当前日期进行格式化操作
    }

    public String getDateComplete() { // 得到的是一个日期：格式为：yyyy年MM月dd日 HH时mm分ss秒SSS毫秒
        this.sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");
        return this.sdf.format(new Date());// 将当前日期进行格式化操作
    }

    public String getTimeStamp() { // 得到的是一个时间戳
        this.sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return this.sdf.format(new Date());// 将当前日期进行格式化操作
    }

}

public class DateDemo07 {

    public static void main(String[] args) {
        DateTime2 dt = new DateTime2();
        System.out.println("系统日期：" + dt.getDate());
        System.out.println("中文日期：" + dt.getDateComplete());
        System.out.println("时间戳：" + dt.getTimeStamp());
    }

}
