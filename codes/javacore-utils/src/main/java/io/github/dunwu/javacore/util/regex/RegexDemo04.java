package io.github.dunwu.javacore.util.regex;

import java.util.regex.Pattern;

/**
 * 示例：Pattern.split() 按正则拆分字符串。
 */
public class RegexDemo04 {

    /**
     * 演示按数字拆分字符串，取出其中的字母。
     */
    public static void demo() {
        // 要求将里面的字符取出，也就是说按照数字拆分
        String str = "A1B22C333D4444E55555F"; // 指定好一个字符串
        String pat = "\\d+"; // 指定好正则表达式
        Pattern p = Pattern.compile(pat); // 实例化Pattern类
        String[] s = p.split(str); // 执行拆分操作
        for (int x = 0; x < s.length; x++) {
            System.out.print(s[x] + "\t");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
