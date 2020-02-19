/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.util.locale;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateFormat 是日期、时间格式化类的抽象类。它支持基于语言习惯的日期、时间格式。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/25.
 */
public class DateFormatDemo {

    public static void main(String[] args) {
        Date date = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, df.format(date));
        System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, df2.format(date));
    }

}
