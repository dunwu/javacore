package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 拼接的高性能写法——始终只修改同一个对象，与 StringBufferDemo09 对比。
 */
public class StringBufferDemo10 {

    /** 演示 StringBuffer 循环拼接：只修改一个对象，结果与 StringBufferDemo09 相同。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer();
        buf.append("Zhang Peng");
        for (int i = 0; i < 100; i++) {
            buf.append(i); // StringBuffer可以修改，性能高
        }
        System.out.println(buf);
    }

    public static void main(String[] args) {
        demo();
    }

}
