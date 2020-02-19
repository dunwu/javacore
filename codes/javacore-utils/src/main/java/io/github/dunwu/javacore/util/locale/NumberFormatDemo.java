/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.util.locale;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * NumberFormat 是所有数字格式类的基类。它提供格式化和解析数字的接口。它也提供了决定数字所属语言类型的方法。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/25.
 */
public class NumberFormatDemo {

    public static void main(String[] args) {
        double num = 123456.78;
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.SIMPLIFIED_CHINESE);
        System.out.format("%f 的本地化（%s）结果: %s", num, Locale.SIMPLIFIED_CHINESE, format.format(num));
    }

}
