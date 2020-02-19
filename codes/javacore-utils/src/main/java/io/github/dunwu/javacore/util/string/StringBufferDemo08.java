package io.github.dunwu.javacore.util.string;

public class StringBufferDemo08 {

    public static void main(String[] args) {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ").append("World!!"); // 向StringBuffer添加内容
        if (buf.indexOf("Hello") == -1) {
            System.out.println("没有查找到指定的内容");
        } else { // 不为01表示查找到内容
            System.out.println("可以查找到指定的内容");
        }
    }

}
