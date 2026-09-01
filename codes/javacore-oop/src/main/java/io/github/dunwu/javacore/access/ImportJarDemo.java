package io.github.dunwu.javacore.access;

/**
 * 示例：jar 包中的类同样通过 import（或全限定名）使用——对调用方而言，
 * 类来自当前工程还是来自 jar 包并无区别。
 */
public class ImportJarDemo {

    /**
     * 演示像使用本地类一样使用 jar 包中的类。
     */
    public static void demo() {
        Hello hello = new Hello();
        System.out.println(hello.getInfo());
    }

    public static void main(String[] args) {
        demo();
    }

}
