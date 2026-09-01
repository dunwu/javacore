package io.github.dunwu.javacore.access;

import io.github.dunwu.javacore.object.Person2;

/**
 * 示例：导入项目内其他包的自定义类。
 */
public class ImportDemo02 {

    /**
     * 演示导入项目内其他包的类并使用。
     */
    public static void demo() {
        // Person2 来自 io.github.dunwu.javacore.object 包，需先 import 才能使用
        Person2 hello = new Person2("张三", 30);
        hello.tell();
    }

    public static void main(String[] args) {
        demo();
    }

}
