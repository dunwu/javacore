package io.github.dunwu.javacore.util.regex;

import java.util.regex.Pattern;

/**
 * 示例：用正则判断字符串是否全为数字——一行代码完成（对比 RegexDemo01 的逐字符写法）。
 */
public class RegexDemo02 {

    /**
     * 演示正则验证字符串是否全由数字组成。
     */
    public static void demo() {
        String str = "1234567890"; // 此字符串由数字组成
        if (Pattern.compile("[0-9]+").matcher(str).matches()) { // 使用正则
            System.out.println("是由数字组成！");
        } else {
            System.out.println("不是由数字组成！");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
