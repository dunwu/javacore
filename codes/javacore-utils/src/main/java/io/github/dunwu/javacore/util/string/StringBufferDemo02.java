package io.github.dunwu.javacore.util.string;

public class StringBufferDemo02 {

    public static void main(String[] args) {
        StringBuffer buf = new StringBuffer(); // 声明StringBuffer对象
        buf.append("Hello ");
        fun(buf); // 传递StringBuffer内容
        System.out.println(buf); // 打印内容
    }

    public static void fun(StringBuffer s) { // 接收StringBuffer引用
        s.append("JAVA ").append("Zhang Peng"); // 修改StringBuffer的内容
    }

}
