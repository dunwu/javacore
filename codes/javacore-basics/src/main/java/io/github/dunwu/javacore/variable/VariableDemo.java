package io.github.dunwu.javacore.variable;

/**
 * 示例：Java 中变量的三种作用域 —— 静态变量（类共享）、成员变量（每个对象独有）、局部变量（方法内，含参数）。
 */
public class VariableDemo {

    // 静态变量：属于类，所有实例共享，无需创建对象即可访问
    private static String v1 = "静态变量";

    // 成员变量：属于对象实例，随对象创建而初始化
    private String v2 = "成员变量";

    /**
     * 演示三种变量的访问：静态变量、成员变量、局部变量（含方法参数）。
     */
    public void test(String v4) {
        // 局部变量：仅在方法内有效，方法结束即销毁
        String v3 = "局部变量";
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);
        System.out.println(v4);
    }

    public static void main(String[] args) {
        VariableDemo demo = new VariableDemo();
        demo.test("参数变量");
    }

}
