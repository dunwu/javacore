package io.github.dunwu.jvm.classloader;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init!");
    }
}
