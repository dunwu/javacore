package io.github.dunwu.javacore.i18n;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateFormatDemo 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-12-23
 */
public class DateFormatDemo {

    public static void main(String[] args) {
        Date date = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        System.out.format("%s 的国际化（%s）结果: %s\n", date, Locale.ENGLISH, df.format(date));
        System.out.format("%s 的国际化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, df2.format(date));
    }

}

// 输出
// Fri Dec 23 11:14:45 CST 2022 的国际化（en）结果: Dec 23, 2022
// Fri Dec 23 11:14:45 CST 2022 的国际化（zh_CN）结果: 2022-12-23
