package io.github.dunwu.javacore.variable;

public class VariableDemo {

    // 静态变量
    private static String v1 = "静态变量";

    // 成员变量
    private String v2 = "成员变量";

    public void test(String v4) {
        // 局部变量
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
