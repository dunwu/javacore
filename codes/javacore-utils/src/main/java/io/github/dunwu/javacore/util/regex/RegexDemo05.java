package io.github.dunwu.javacore.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 示例：Matcher.replaceAll() 按正则替换字符串中的匹配内容。
 */
public class RegexDemo05 {

    /**
     * 演示把字符串中的数字全部替换为下划线。
     */
    public static void demo() {
        // 要求将里面的数字替换掉
        String str = "A1B22C333D4444E55555F"; // 指定好一个字符串
        String pat = "\\d+"; // 指定好正则表达式
        Pattern p = Pattern.compile(pat); // 实例化Pattern类
        Matcher m = p.matcher(str); // 实例化Matcher类的对象
        String newString = m.replaceAll("_");
        System.out.println(newString);
    }

    public static void main(String[] args) {
        demo();
    }

}
