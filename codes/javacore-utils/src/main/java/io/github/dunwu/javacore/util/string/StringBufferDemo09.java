package io.github.dunwu.javacore.util.string;

public class StringBufferDemo09 {

    public static void main(String[] args) {
        String str1 = "Zhang Peng";
        for (int i = 0; i < 100; i++) {
            str1 += i; // 不断修改String的内存引用，性能低
        }
        System.out.println(str1);
    }

}
