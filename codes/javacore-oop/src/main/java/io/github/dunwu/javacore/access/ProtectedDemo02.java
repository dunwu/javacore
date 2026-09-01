package io.github.dunwu.javacore.access;

/**
 * 示例：protected 成员不能被不同包的非子类访问（反例说明，下方被注释的语句无法编译）。
 */
public class ProtectedDemo02 {

    /**
     * 演示 protected 的访问边界：不同包的非子类无法访问。
     */
    public static void demo() {
        Hello sub = new Hello();
        // 【错误用法】不同包的非子类无法访问 protected 属性，以下语句编译不通过：
        // System.out.println(sub.name); // 错误的，不同包的类无法访问（除非是子类）
        System.out.println("protected 属性只允许同包类和子类访问，本类只能调用 public 方法：" + sub.getInfo());
    }

    public static void main(String[] args) {
        demo();
    }

}
