package io.github.dunwu.javacore.util.regex;

public class RegexDemo01 {

    public static void main(String[] args) {
        String str = "1234567890"; // 此字符串由数字组成
        boolean flag = true; // 定义一个标记变量
        // 要先将字符串拆分成字符数组，之后依次判断
        char[] c = str.toCharArray(); // 将字符串变为字符数组
        for (int i = 0; i < c.length; i++) { // 循环依次判断
            if (c[i] < '0' || c[i] > '9') { // 如果满足条件，则表示不是数字
                flag = false; // 做个标记
                break; // 程序不再向下继续执行
            }
        }
        if (flag) {
            System.out.println("是由数字组成！");
        } else {
            System.out.println("不是由数字组成！");
        }
    }

}
