package io.github.dunwu.javacore.method;

/**
 * 抽象方法示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class AbstractMethodDemo {

    /**
     * 演示抽象方法：抽象类不能实例化，由子类实现抽象方法。
     */
    public static void demo() {
        AbstractClass demo = new ConcreteClass();
        demo.print();
    }

    public static void main(String[] args) {
        demo();
    }

    static abstract class AbstractClass {

        abstract void print();

    }

    static class ConcreteClass extends AbstractClass {

        @Override
        void print() {
            System.out.println("call print()");
        }

    }

}
// Outpu:
// call print()
