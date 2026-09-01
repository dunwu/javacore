package io.github.dunwu.javacore.access;

/**
 * 示例：同一个包内的类可以直接使用，无需 import。
 */
public class PackageDemo01 {

    /**
     * 演示同包类直接访问。
     */
    public static void demo() {
        // Hello 与本类在同一个包中，无需 import 即可使用
        System.out.println(new Hello().getInfo());
    }

    public static void main(String[] args) {
        demo();
    }

}
