package io.github.dunwu.javacore.util;

/**
 * 示例：System.getProperty() 读取常用系统属性（操作系统、用户、目录等）。
 */
public class SystemDemo03 {

    /**
     * 演示读取常用系统属性。
     */
    public static void demo() {
        System.out.println("系统版本：" + System.getProperty("os.name") + System.getProperty("os.version")
            + System.getProperty("os.arch"));
        System.out.println("系统用户：" + System.getProperty("user.name"));
        System.out.println("当前用户目录：" + System.getProperty("user.home"));
        System.out.println("当前用户工作目录：" + System.getProperty("user.dir"));
    }

    public static void main(String[] args) {
        demo();
    }

}
