package io.github.dunwu.javacore.util.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

class DateTime {

    // 以后直接通过此类就可以取得日期时间
    private Calendar calendar = null; // 声明一个Calendar对象，取得时间

    public DateTime() { // 构造方法中直接实例化对象
        this.calendar = new GregorianCalendar();
    }

    public String getDate() { // 得到的是一个日期：格式为：yyyy-MM-dd HH:mm:ss.SSS
        // 考虑到程序要频繁修改字符串，所以使用StringBuffer提升性能
        StringBuffer buf = new StringBuffer();
        buf.append(calendar.get(Calendar.YEAR)).append("-"); // 增加年
        buf.append(this.addZero(calendar.get(Calendar.MONTH) + 1, 2)).append("-"); // 增加月
        buf.append(this.addZero(calendar.get(Calendar.DAY_OF_MONTH), 2)).append(" "); // 取得日
        buf.append(this.addZero(calendar.get(Calendar.HOUR_OF_DAY), 2)).append(":"); // 取得时
        buf.append(this.addZero(calendar.get(Calendar.MINUTE), 2)).append(":");
        buf.append(this.addZero(calendar.get(Calendar.SECOND), 2)).append(".");
        buf.append(this.addZero(calendar.get(Calendar.MILLISECOND), 3));
        return buf.toString();
    }

    // 考虑到日期中存在前导0，所以在此处加上补零的方法
    private String addZero(int num, int len) {
        StringBuffer s = new StringBuffer();
        s.append(num);
        while (s.length() < len) { // 如果长度不足，则继续补0
            s.insert(0, "0"); // 在第一个位置处补0
        }
        return s.toString();
    }

    public String getDateComplete() { // 得到的是一个日期：格式为：yyyy年MM月dd日 HH时mm分ss秒SSS毫秒
        // 考虑到程序要频繁修改字符串，所以使用StringBuffer提升性能
        StringBuffer buf = new StringBuffer();
        buf.append(calendar.get(Calendar.YEAR)).append("年"); // 增加年
        buf.append(this.addZero(calendar.get(Calendar.MONTH) + 1, 2)).append("月"); // 增加月
        buf.append(this.addZero(calendar.get(Calendar.DAY_OF_MONTH), 2)).append("日"); // 取得日
        buf.append(this.addZero(calendar.get(Calendar.HOUR_OF_DAY), 2)).append("时"); // 取得时
        buf.append(this.addZero(calendar.get(Calendar.MINUTE), 2)).append("分"); // 取得分
        buf.append(this.addZero(calendar.get(Calendar.SECOND), 2)).append("秒"); // 取得秒
        buf.append(this.addZero(calendar.get(Calendar.MILLISECOND), 3)).append("毫秒"); // 取得毫秒
        return buf.toString();
    }

    public String getTimeStamp() { // 得到的是一个时间戳
        // 考虑到程序要频繁修改字符串，所以使用StringBuffer提升性能
        StringBuffer buf = new StringBuffer();
        buf.append(calendar.get(Calendar.YEAR)); // 增加年
        buf.append(this.addZero(calendar.get(Calendar.MONTH) + 1, 2)); // 增加月
        buf.append(this.addZero(calendar.get(Calendar.DAY_OF_MONTH), 2)); // 取得日
        buf.append(this.addZero(calendar.get(Calendar.HOUR_OF_DAY), 2)); // 取得时
        buf.append(this.addZero(calendar.get(Calendar.MINUTE), 2)); // 取得分
        buf.append(this.addZero(calendar.get(Calendar.SECOND), 2)); // 取得秒
        buf.append(this.addZero(calendar.get(Calendar.MILLISECOND), 3)); // 取得毫秒
        return buf.toString();
    }

}

public class DateDemo06 {

    public static void main(String[] args) {
        DateTime dt = new DateTime();
        System.out.println("系统日期：" + dt.getDate());
        System.out.println("中文日期：" + dt.getDateComplete());
        System.out.println("时间戳：" + dt.getTimeStamp());
    }

}
