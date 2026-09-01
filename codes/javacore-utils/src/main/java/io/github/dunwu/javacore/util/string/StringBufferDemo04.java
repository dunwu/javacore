package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的 reverse()——将内容反转。
 */
public class StringBufferDemo04 {

    /** 演示 reverse() 将 StringBuffer 内容倒序。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("World!!"); // 添加内容
        buf.insert(0, "Hello "); // 在第一个内容之前添加内容
        String str = buf.reverse().toString(); // 将内容反转后变为String类型
        System.out.println(str); // 将内容输出
    }

    public static void main(String[] args) {
        demo();
    }

}
