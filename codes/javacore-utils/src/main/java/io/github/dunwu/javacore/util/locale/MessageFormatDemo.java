/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.util.locale;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Messageformat 提供一种与语言无关的拼接消息的方式。通过这种拼接方式，将最终呈现返回给使用者。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/25.
 */
public class MessageFormatDemo {

    public static void main(String[] args) {
        String pattern1 = "{0}，你好！你于  {1} 消费  {2} 元。";
        String pattern2 = "At {1,time,short} On {1,date,long}，{0} paid {2,number, currency}.";
        Object[] params = { "Jack", new GregorianCalendar().getTime(), 8888 };
        String msg1 = MessageFormat.format(pattern1, params);
        MessageFormat mf = new MessageFormat(pattern2, Locale.US);
        String msg2 = mf.format(params);
        System.out.println(msg1);
        System.out.println(msg2);
    }

}
