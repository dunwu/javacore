package io.github.dunwu.javacore.object;

public class ObjectRefDemo02 {

    public static void main(String[] args) {
        String str1 = "hello";
        System.out.println("fun()方法调用之前：" + str1);
        fun(str1);
        System.out.println("fun()方法调用之后：" + str1);
    }

    private static void fun(String str2) {
        str2 = "javase";
    }

}
