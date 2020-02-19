package io.github.dunwu.javacore.jvm.classloader;

public class SubClass extends SuperClass {

    static {
        System.out.println("SubClass init!");
    }
}
