package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的 replace()——替换指定范围的内容。
 */
public class StringBufferDemo05 {

    /** 演示 replace() 将 "World" 替换为新内容。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ").append("World!!"); // 向StringBuffer添加内容
        buf.replace(6, 11, "Zhang Peng"); // 将world的内容替换
        System.out.println("内容替换之后的结果：" + buf); // 输出内容
    }

    public static void main(String[] args) {
        demo();
    }

}
