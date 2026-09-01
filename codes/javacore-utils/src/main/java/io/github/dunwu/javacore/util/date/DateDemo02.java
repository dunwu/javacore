package io.github.dunwu.javacore.util.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 示例：Calendar 可以按字段（年、月、日、时、分、秒）拆分日期时间。
 * <p>
 * 注意：Calendar.MONTH 从 0 开始计数，需要 +1 才是实际月份。
 */
public class DateDemo02 {

    /**
     * 演示按字段取得当前日期时间。
     */
    public static void demo() {
        Calendar calendar = new GregorianCalendar(); // 实例化Calendar类对象
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + (calendar.get(Calendar.MONTH) + 1)); // MONTH 从 0 开始，需要 +1
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
    }

    public static void main(String[] args) {
        demo();
    }

}
