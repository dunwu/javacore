package io.github.dunwu.javacore.method;

/**
 * 抽象方法示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class AbstractMethodDemo {

    public static void main(String[] args) {
        AbstractClass demo = new ConcreteClass();
        demo.print();
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
