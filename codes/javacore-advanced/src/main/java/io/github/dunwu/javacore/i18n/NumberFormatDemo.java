package io.github.dunwu.javacore.i18n;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * NumberFormat 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-12-23
 */
public class NumberFormatDemo {

    public static void main(String[] args) {
        double num = 123456.78;
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.SIMPLIFIED_CHINESE);
        System.out.format("%f 的国际化（%s）结果: %s\n", num, Locale.SIMPLIFIED_CHINESE, format.format(num));
    }

}

// 输出：
// 123456.780000 的国际化（zh_CN）结果: ￥123,456.78
