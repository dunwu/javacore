package io.github.dunwu.javacore.jvm.classloader;

/**
 * 常量在编译阶段会存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化。
 */
public class ConstClass {

    public static final String value = "hello world";

    static {
        System.out.println("ConstClass init!");
    }
}
