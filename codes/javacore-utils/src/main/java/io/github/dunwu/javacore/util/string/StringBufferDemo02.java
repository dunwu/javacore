package io.github.dunwu.javacore.util.string;

/**
 * 示例：StringBuffer 的引用传递——把 StringBuffer 传入方法后在方法内修改，原对象同步改变。
 */
public class StringBufferDemo02 {

    /** 演示 StringBuffer 作为引用类型参数传递时，方法内的修改会影响原对象。 */
    public static void demo() {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ");
        fun(buf); // 传递StringBuffer内容
        System.out.println(buf); // 打印内容
    }

    public static void fun(StringBuffer s) { // 接收StringBuffer引用
        s.append("JAVA ").append("Zhang Peng"); // 修改StringBuffer的内容
    }

    public static void main(String[] args) {
        demo();
    }

}
