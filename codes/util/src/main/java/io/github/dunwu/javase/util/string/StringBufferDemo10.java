package io.github.dunwu.javase.util.string;

public class StringBufferDemo10 {
    public static void main(String[] args) {
        StringBuffer buf = new StringBuffer();
        buf.append("Zhang Peng");
        for (int i = 0; i < 100; i++) {
            buf.append(i); // StringBuffer可以修改，性能高
        }
        System.out.println(buf);
    }
};
