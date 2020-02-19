package io.github.dunwu.javacore.util.string;

public class StringBufferDemo01 {

    public static void main(String[] args) {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello "); // 向StringBuffer中添加内容
        buf.append("World").append("!!!"); // 可以连续调用append()方法
        buf.append("\n"); // 添加一个转义字符
        buf.append("数字 = ").append(1).append("\n"); // 添加数字
        buf.append("字符 = ").append('C').append("\n"); // 添加字符
        buf.append("布尔 = ").append(true); // 添加布尔值
        System.out.println(buf); // 直接输出对象，调用toString()
    }

}
