package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的 insert()——在指定位置插入内容。
 */
public class StringBufferDemo03 {

    /** 演示 insert() 在开头和末尾插入内容。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("World!!"); // 添加内容
        buf.insert(0, "Hello "); // 在第一个内容之前添加内容
        System.out.println(buf);
        buf.insert(buf.length(), "JAVA~"); // 在最后添加内容
        System.out.println(buf);
    }

    public static void main(String[] args) {
        demo();
    }

}
