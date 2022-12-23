package io.github.dunwu.javacore.i18n;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * MessageFormat 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-12-23
 */
public class MessageFormatDemo {

    public static void main(String[] args) {
        String pattern1 = "{0}，你好！你于 {1} 消费 {2} 元。";
        String pattern2 = "At {1,time,short} On {1,date,long}，{0} paid {2,number, currency}.";
        Object[] params = { "Jack", new GregorianCalendar().getTime(), 8888 };
        String msg1 = MessageFormat.format(pattern1, params);
        MessageFormat mf = new MessageFormat(pattern2, Locale.US);
        String msg2 = mf.format(params);
        System.out.println(msg1);
        System.out.println(msg2);
    }

}

// 输出：
// Jack，你好！你于 22-12-23 上午11:05 消费 8,888 元。
// At 11:05 AM On December 23, 2022，Jack paid $8,888.00.
