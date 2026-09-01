package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的 delete()——删除指定范围的内容。
 */
public class StringBufferDemo07 {

    /** 演示 delete() 删除指定范围的内容。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ").append("World!!"); // 向StringBuffer添加内容
        buf.replace(6, 11, "Zhang Peng"); // 将world的内容替换
        String str = buf.delete(6, 15).toString(); // 删除指定范围中的内容
        System.out.println("删除之后的结果：" + str); // 输出内容
    }

    public static void main(String[] args) {
        demo();
    }

}
