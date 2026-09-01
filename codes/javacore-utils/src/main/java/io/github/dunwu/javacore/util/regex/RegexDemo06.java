package io.github.dunwu.javacore.util.regex;

/**
 * 示例：String 类内置的正则快捷方法——replaceAll() 替换、matches() 验证、split() 拆分。
 */
public class RegexDemo06 {

    /** 演示 String 中基于正则的替换、验证和拆分。 */
    public static void demo() {
        // replaceAll()：把一段或多段连续数字都替换为下划线
        String str1 = "A1B22C333D4444E55555F".replaceAll("\\d+", "_");
        // matches()：验证整个字符串是否符合"年-月-日"格式
        boolean temp = "1983-07-27".matches("\\d{4}-\\d{2}-\\d{2}");
        // split()：按一段或多段连续数字拆分字符串
        String[] s = "A1B22C333D4444E55555F".split("\\d+");
        System.out.println("字符串替换操作：" + str1);
        System.out.println("字符串验证：" + temp);
        System.out.print("字符串的拆分：");
        for (int x = 0; x < s.length; x++) {
            System.out.print(s[x] + "\t");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
