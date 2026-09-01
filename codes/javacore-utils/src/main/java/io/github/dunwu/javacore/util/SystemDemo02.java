package io.github.dunwu.javacore.util;

/**
 * 示例：System.getProperties() 可以取得 JVM 从操作系统读取的全部系统属性。
 */
public class SystemDemo02 {

    /**
     * 演示列出全部系统属性。
     */
    public static void demo() {
        System.getProperties().list(System.out); // 列出系统的全部属性
    }

    public static void main(String[] args) {
        demo();
    }

}
