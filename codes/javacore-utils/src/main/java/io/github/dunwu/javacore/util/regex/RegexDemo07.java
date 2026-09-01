package io.github.dunwu.javacore.util.regex;

/**
 * 示例：多级拆分——先按 | 拆分组，再按 : 拆分键值对。
 * <p>
 * 注意：| 在正则中表示"或"，拆分时必须转义写成 "\\|"。
 */
public class RegexDemo07 {

    /** 演示用两级 split() 解析 "LXH:98|JAVA:90|LI:100" 形式的字符串。 */
    public static void demo() {
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

    public static void main(String[] args) {
        demo();
    }

}
