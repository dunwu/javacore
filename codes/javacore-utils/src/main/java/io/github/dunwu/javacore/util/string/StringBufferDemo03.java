package io.github.dunwu.javacore.util.string;

public class StringBufferDemo03 {

    public static void main(String[] args) {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("World!!"); // 添加内容
        buf.insert(0, "Hello "); // 在第一个内容之前添加内容
        System.out.println(buf);
        buf.insert(buf.length(), "JAVA~"); // 在最后添加内容
        System.out.println(buf);
    }

}
