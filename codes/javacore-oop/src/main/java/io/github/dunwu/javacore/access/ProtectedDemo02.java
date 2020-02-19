package io.github.dunwu.javacore.access;

public class ProtectedDemo02 {

    public static void main(String[] args) {
        Hello sub = new Hello();
        // System.out.println(sub.name); // 错误的，不同包的类无法访问
    }

}
