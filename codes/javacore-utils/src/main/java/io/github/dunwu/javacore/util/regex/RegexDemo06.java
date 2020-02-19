package io.github.dunwu.javacore.util.regex;

public class RegexDemo06 {

    public static void main(String[] args) {
        String str1 = "A1B22C333D4444E55555F".replaceAll("\\d+", "_");
        boolean temp = "1983-07-27".matches("\\d{4}-\\d{2}-\\d{2}");
        String[] s = "A1B22C333D4444E55555F".split("\\d+");
        System.out.println("字符串替换操作：" + str1);
        System.out.println("字符串验证：" + temp);
        System.out.print("字符串的拆分：");
        for (int x = 0; x < s.length; x++) {
            System.out.print(s[x] + "\t");
        }
    }

}
