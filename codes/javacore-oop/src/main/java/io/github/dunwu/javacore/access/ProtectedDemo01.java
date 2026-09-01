package io.github.dunwu.javacore.access;

class SubHelloDemo extends Hello {

    public void print() {
        // 子类可以直接访问父类的 protected 属性（即使跨包）
        System.out.println("访问受保护属性：" + super.name);
    }

}

/**
 * 示例：protected 成员可被子类访问——即使不在同一个包中，子类也能访问父类的 protected 属性。
 */
public class ProtectedDemo01 {

    /**
     * 演示子类访问父类 protected 属性。
     */
    public static void demo() {
        SubHelloDemo sub = new SubHelloDemo();
        sub.print();
    }

    public static void main(String[] args) {
        demo();
    }

}
