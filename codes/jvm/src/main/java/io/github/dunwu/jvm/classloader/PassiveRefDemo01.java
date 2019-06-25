package io.github.dunwu.jvm.classloader;

/**
 * 非主动使用类字段演示
 **/
public class PassiveRefDemo01 {

    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}
