package io.github.dunwu.javacore.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDemo03 {

    public static void main(String[] args) {
        String str = "1983-07-27"; // 指定好一个日期格式的字符串
        String pat = "\\d{4}-\\d{2}-\\d{2}"; // 指定好正则表达式
        Pattern p = Pattern.compile(pat); // 实例化Pattern类
        Matcher m = p.matcher(str); // 实例化Matcher类
        if (m.matches()) { // 进行验证的匹配，使用正则
            System.out.println("日期格式合法！");
        } else {
            System.out.println("日期格式不合法！");
        }
    }

}
