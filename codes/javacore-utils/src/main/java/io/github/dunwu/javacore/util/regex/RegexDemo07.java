package io.github.dunwu.javacore.util.regex;

public class RegexDemo07 {

    public static void main(String[] args) {
        String info = "LXH:98|JAVA:90|LI:100"; // 定义一个字符串
        // 拆分的形式：
        /*
         * LXH --> 98 JAVA --> 90 LI --> 100
         */
        String[] s = info.split("\\|");
        System.out.println("字符串的拆分：");
        for (int x = 0; x < s.length; x++) {
            String[] s2 = s[x].split(":");
            System.out.println(s2[0] + "\t" + s2[1]);
        }
    }

}
