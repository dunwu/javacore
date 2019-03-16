package io.github.dunwu.javacore.method;

/**
 * 抽象方法示例
 * @author Zhang Peng
 * @date 2019-03-16
 */
public class AbstractMethodDemo {
    static abstract class AbstractClass {
        abstract void print();
    }

    static class ConcreteClass extends AbstractClass {
        @Override
        void print() {
            System.out.println("call print()");
        }
    }

    public static void main(String[] args) {
        AbstractClass demo = new ConcreteClass();
        demo.print();
    }

}
// Outpu:
// call print()
