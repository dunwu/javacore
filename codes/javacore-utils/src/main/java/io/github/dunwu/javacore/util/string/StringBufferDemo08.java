package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的 indexOf()——查找内容是否存在。
 */
public class StringBufferDemo08 {

    /** 演示 indexOf() 查找内容，返回 -1 表示未找到。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ").append("World!!"); // 向StringBuffer添加内容
        if (buf.indexOf("Hello") == -1) {
            System.out.println("没有查找到指定的内容");
        } else { // 不为 -1 表示查找到内容
            System.out.println("可以查找到指定的内容");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
